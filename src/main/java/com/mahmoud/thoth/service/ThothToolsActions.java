package com.mahmoud.thoth.service;

import com.mahmoud.thoth.domain.port.in.CreateNamespaceRequest;
import com.mahmoud.thoth.domain.port.out.NameSpaceListViewDto;
import com.mahmoud.thoth.domain.service.*;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.List;





/**
 * AI-powered service for managing document buckets/namespaces through natural language.
 * Uses function calling to execute appropriate use cases based on user queries.
 */

@Service
public class ThothToolsActions {

    private final CreateNamespaceUseCase createNamespaceUseCase;
    private final DeleteNamespaceUseCase deleteNamespaceUseCase;
    private final NamespaceQueryUseCase namespaceQueryUseCase;
    
    // In-memory storage for demo purposes

    public ThothToolsActions(
            CreateBucketUseCase createBucketUseCase,
            DeleteBucketUseCase deleteBucketUseCase,
            BucketQueryUseCase bucketQueryUseCase,
            CreateNamespaceUseCase createNamespaceUseCase,
            DeleteNamespaceUseCase deleteNamespaceUseCase,
            NamespaceQueryUseCase namespaceQueryUseCase) {

       
        this.createNamespaceUseCase = createNamespaceUseCase;
        this.deleteNamespaceUseCase = deleteNamespaceUseCase;
        this.namespaceQueryUseCase = namespaceQueryUseCase;
        
       
    }

    @Tool(description = "Lists all available namespaces",returnDirect = true)
    public List<NameSpaceListViewDto> listNamespaces() {
        return namespaceQueryUseCase.findAll();
    }

    @Tool(description = "Creates a new namespace with the given name",returnDirect = true)
    public String createNamespace(@ToolParam(description = "Name of the namespace to create") String nameSpaceName) {
        CreateNamespaceRequest createRequest = new CreateNamespaceRequest();
        createRequest.setNamespaceName(nameSpaceName);
        var result = createNamespaceUseCase.execute(createRequest);
        return "Namespace created successfully with ID: " + result.getId();
    }

    @Tool(description = "Deletes a namespace by its ID" , returnDirect = true)
    public String deleteNamespace(@ToolParam(description = "ID of the namespace to delete") Long nameSpaceId) {
        deleteNamespaceUseCase.execute(nameSpaceId);
        return "Namespace with ID " + nameSpaceId + " deleted successfully";
    }

}
