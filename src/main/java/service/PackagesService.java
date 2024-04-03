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
        resultPair.getKey().stream().map(this::toJson).forEach(
                System.out::println
        );
        resultPair.getValue().stream().map(this::toJson).forEach(
                System.out::println
        );
        var fstMap = firstPackages.getBody().getPackages().stream().collect(Collectors.toMap(
                PackageDto::getName, dto -> dto.getVersion() + "-" + dto.getRelease()
        ));
        var sndMap = secondPackages.getBody().getPackages().stream().collect(Collectors.toMap(
                PackageDto::getName, dto -> dto.getVersion() + "-" + dto.getRelease()
        ));
        for (var name : fstMap.keySet()) {
            if (sndMap.containsKey(name) && fstMap.get(name).compareTo(sndMap.get(name)) > 0) {
                System.out.println(name + ", " + fstMap.get(name) + ", " + sndMap.get(name));
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
