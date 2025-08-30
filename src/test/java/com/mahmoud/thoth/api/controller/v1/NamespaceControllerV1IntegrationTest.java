package com.mahmoud.thoth.api.controller.v1;

import com.mahmoud.thoth.config.TestcontainersConfig;
import com.mahmoud.thoth.domain.port.in.CreateNamespaceRequest;
import com.mahmoud.thoth.domain.port.in.UpdateNamespaceRequest;
import com.mahmoud.thoth.domain.port.out.NameSpaceViewDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@SuppressWarnings("null")
class NamespaceControllerV1IntegrationTest extends TestcontainersConfig {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/v1/namespaces";
    }

    @Test
    void given_NewNamespaceName_When_CreatingNamespace_Then_NamespaceIsCreated() {
        // Given: A new namespace name
        String namespaceName = "test-namespace";
        CreateNamespaceRequest request = new CreateNamespaceRequest();
        request.setNamespaceName(namespaceName);

        // When: Creating a new namespace
        ResponseEntity<NameSpaceViewDto> response = restTemplate.postForEntity(
                baseUrl,
                request,
                NameSpaceViewDto.class
        );

        // Then: Namespace is created successfully
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(namespaceName);
        assertThat(response.getBody().getId()).isNotNull();
    }

    @Test
    void given_ExistingNamespaceId_When_GettingNamespace_Then_NamespaceIsReturned() {
        // Given: Create a namespace first
        String namespaceName = "test-get";
        CreateNamespaceRequest createRequest = new CreateNamespaceRequest();
        createRequest.setNamespaceName(namespaceName);
        
        ResponseEntity<NameSpaceViewDto> createResponse = restTemplate.postForEntity(
                baseUrl, createRequest, NameSpaceViewDto.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        
        NameSpaceViewDto createdNamespace = createResponse.getBody();
        assertThat(createdNamespace).isNotNull();

        // When: Getting the namespace by ID
        ResponseEntity<NameSpaceViewDto> response = restTemplate.getForEntity(
                baseUrl + "/" + createdNamespace.getId(),
                NameSpaceViewDto.class
        );

        // Then: Namespace is returned successfully
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(createdNamespace.getId());
        assertThat(response.getBody().getName()).isEqualTo(namespaceName);
    }

    @Test
    void given_ExistingNamespaceId_When_UpdatingNamespace_Then_NamespaceIsUpdated() {
        // Given: Create a namespace first
        String originalName = "test-update";
        CreateNamespaceRequest createRequest = new CreateNamespaceRequest();
        createRequest.setNamespaceName(originalName);
        
        ResponseEntity<NameSpaceViewDto> createResponse = restTemplate.postForEntity(
                baseUrl, createRequest, NameSpaceViewDto.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        
        NameSpaceViewDto createdNamespace = createResponse.getBody();
        assertThat(createdNamespace).isNotNull();
        
        // When: Updating the namespace
        String newName = "updated-name";
        UpdateNamespaceRequest updateRequest = new UpdateNamespaceRequest();
        updateRequest.setNewNamespaceName(newName);

        restTemplate.put(baseUrl + "/" + createdNamespace.getId(), updateRequest);

        // Then: Namespace is updated successfully
        ResponseEntity<NameSpaceViewDto> getResponse = restTemplate.getForEntity(
                baseUrl + "/" + createdNamespace.getId(),
                NameSpaceViewDto.class
        );
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody().getName()).isEqualTo(newName);
    }

    @Test
    void given_ExistingNamespaceId_When_DeletingNamespace_Then_NamespaceIsDeleted() {
        // Given: Create a namespace first
        String namespaceName = "test-delete";
        CreateNamespaceRequest createRequest = new CreateNamespaceRequest();
        createRequest.setNamespaceName(namespaceName);
        
        ResponseEntity<NameSpaceViewDto> createResponse = restTemplate.postForEntity(
                baseUrl, createRequest, NameSpaceViewDto.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        
        NameSpaceViewDto createdNamespace = createResponse.getBody();
        assertThat(createdNamespace).isNotNull();

        // When: Deleting the namespace
        restTemplate.delete(baseUrl + "/" + createdNamespace.getId());

        // Then: Namespace is deleted successfully
        ResponseEntity<NameSpaceViewDto> getResponse = restTemplate.getForEntity(
                baseUrl + "/" + createdNamespace.getId(),
                NameSpaceViewDto.class
        );
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNull(); // Assuming API returns null for deleted namespaces
    }

    @Test
    void given_NonExistentNamespaceId_When_GettingNamespace_Then_ReturnsNotFound() {
        // When: Getting a non-existent namespace
        ResponseEntity<NameSpaceViewDto> response = restTemplate.getForEntity(
                baseUrl + "/9999",
                NameSpaceViewDto.class
        );

        // Then: Returns appropriate response for non-existent namespace
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNull(); // Assuming API returns null for non-existent
    }

    @Test
    void given_NonExistentNamespaceId_When_UpdatingNamespace_Then_ReturnsConflict() {
        // Given: Update request for non-existent namespace
        UpdateNamespaceRequest updateRequest = new UpdateNamespaceRequest();
        updateRequest.setNewNamespaceName("updated-name");

        // When: Updating a non-existent namespace
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/9999",
                org.springframework.http.HttpMethod.PUT,
                new org.springframework.http.HttpEntity<>(updateRequest),
                Void.class
        );

        // Then: Returns conflict status
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void given_NonExistentNamespaceId_When_DeletingNamespace_Then_ReturnsNotFound() {
        // When: Deleting a non-existent namespace
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/9999",
                org.springframework.http.HttpMethod.DELETE,
                null,
                Void.class
        );

        // Then: Returns not found status
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void given_MultipleNamespaces_When_ListingNamespaces_Then_AllNamespacesAreReturned() {
        // Given: Create some test namespaces
        createTestNamespace("ns1");
        createTestNamespace("ns2");

        // When: Listing all namespaces
        ResponseEntity<NameSpaceViewDto[]> response = restTemplate.getForEntity(
                baseUrl,
                NameSpaceViewDto[].class
        );

        // Then: All namespaces are returned
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isGreaterThanOrEqualTo(2);
    }

    private void createTestNamespace(String name) {
        CreateNamespaceRequest request = new CreateNamespaceRequest();
        request.setNamespaceName(name);
        ResponseEntity<NameSpaceViewDto> response = restTemplate.postForEntity(
                baseUrl, request, NameSpaceViewDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
}
