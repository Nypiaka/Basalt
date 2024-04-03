package service;

import client.BasaltClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dto.ArchVersionDto;
import dto.PackageDto;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.stream.Collectors;

public class PackagesService {

    private static final String ARCH_INFO_ERROR_MESSAGE = "getting information about architectures";

    private static final String PACKAGES_ERROR_MESSAGE = "getting packages";

    private final BasaltClient client = new BasaltClient();

    public void compareBranches(String firstBranch, String secondBranch) {
        var firstArchs = client.getPackageSet(firstBranch);
        var secondArchs = client.getPackageSet(secondBranch);
        if (!validateResponse(firstArchs, ARCH_INFO_ERROR_MESSAGE, firstBranch)) {
            return;
        }
        if (!validateResponse(secondArchs, ARCH_INFO_ERROR_MESSAGE, secondBranch)) {
            return;
        }
        var commonArchs = collectionsIntersection(Objects.requireNonNull(firstArchs.getBody()).getArchs().stream()
                .map(ArchVersionDto::getArch).toList(), Objects.requireNonNull(secondArchs.getBody()).getArchs().stream()
                .map(ArchVersionDto::getArch).toList());
        for (var arch : commonArchs) {
            System.out.println("_".repeat(20) + "Arch: " + arch + "_".repeat(20));
            comparePackages(arch, firstBranch, secondBranch);
            System.out.println("_".repeat(50));
        }
    }

    private void comparePackages(String arch, String firstBranch, String secondBranch) {
        var firstPackages = client.getBranch(firstBranch, arch);
        var secondPackages = client.getBranch(secondBranch, arch);
        if (!validateResponse(firstPackages, PACKAGES_ERROR_MESSAGE, firstBranch)) {
            return;
        }
        if (!validateResponse(secondPackages, PACKAGES_ERROR_MESSAGE, secondBranch)) {
            return;
        }
        var resultPair = getDifferences(Objects.requireNonNull(firstPackages.getBody()).getPackages(),
                Objects.requireNonNull(secondPackages.getBody()).getPackages());
        System.out.println("_".repeat(10) + "Packages that are in the first branch, but not in the second"
                + "_".repeat(10));
        resultPair.getKey().stream().map(this::toJson).forEach(
                System.out::println
        );
        System.out.println("_".repeat(10) + "Packages that are in the second branch, but not in the first"
                + "_".repeat(10));
        resultPair.getValue().stream().map(this::toJson).forEach(
                System.out::println
        );
        var fstMap = firstPackages.getBody().getPackages().stream().collect(Collectors.toMap(
                PackageDto::getName, dto -> Map.entry(dto.getVersion() + "-" + dto.getRelease(), dto)
        ));
        var sndMap = secondPackages.getBody().getPackages().stream().collect(Collectors.toMap(
                PackageDto::getName, dto -> Map.entry(dto.getVersion() + "-" + dto.getRelease(), dto)
        ));
        System.out.println("_".repeat(7) + "Packages with more version-releases in the first than in the second"
                + "_".repeat(6));
        for (var name : fstMap.keySet()) {
            if (sndMap.containsKey(name) && fstMap.get(name).getKey().compareTo(sndMap.get(name).getKey()) > 0) {
                System.out.println(name + ": ");
                System.out.println(toJson(fstMap.get(name).getValue()));
                System.out.println(toJson(sndMap.get(name).getValue()));
            }
        }
    }

    private boolean validateResponse(ResponseEntity<?> response, String errorMessage, String branch) {
        if (response.getStatusCode().isError()) {
            System.out.println("error " + errorMessage + " for branch :" + branch + ". error: " + response.getStatusCode().value());
            return false;
        }
        return true;
    }

    private <T> Set<T> collectionsIntersection(Collection<T> firstCollection, Collection<T> secondCollection) {
        var curSet = new HashSet<>(firstCollection);
        curSet.retainAll(secondCollection);
        return curSet;
    }

    private <T> Map.Entry<Set<T>, Set<T>> getDifferences(Collection<T> firstCollection, Collection<T> secondCollection) {
        var intersect = collectionsIntersection(firstCollection, secondCollection);
        var fstSet = new HashSet<>(firstCollection);
        var sndSet = new HashSet<>(secondCollection);
        fstSet.removeAll(intersect);
        sndSet.removeAll(intersect);
        return Map.entry(fstSet, sndSet);
    }

    private String toJson(Object obj) {
        var mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
