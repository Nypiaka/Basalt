package client;

import dto.BranchDto;
import dto.PackageSetDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

public class BasaltClient {

    private static final String API_URL = "https://rdb.altlinux.org/api/";

    private static final String BRANCH_PACKS = "export/branch_binary_packages/";

    private static final String BRANCH_ARCHS = "site/all_pkgset_archs_with_src_count";
    private final RestClient client = RestClient.builder().baseUrl(API_URL).build();

    public ResponseEntity<BranchDto> getBranch(String branchName, String arch) {
        return client.get().uri(BRANCH_PACKS + branchName + "?arch=" + arch).retrieve().toEntity(BranchDto.class);
    }

    public ResponseEntity<PackageSetDto> getPackageSet(String branchName) {
        return client.get().uri(BRANCH_ARCHS + "?branch=" + branchName).retrieve().toEntity(PackageSetDto.class);
    }


}
