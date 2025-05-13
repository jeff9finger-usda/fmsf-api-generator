### Java Spring Web Client SDK Example

```java
import gov.firenet.fmsf.api.client.ModelControllerApi;
import gov.firenet.fmsf.api.client.ResourceControllerApi;
import gov.firenet.fmsf.api.client.dto.ModelMetadataDto;
import gov.firenet.fmsf.api.client.dto.ResourceMetadataDto;
import gov.firenet.fmsf.api.client.spring.ApiClient;

import java.io.File;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

class FmsfApiClient {
    private final static Logger LOGGER = Logger.getLogger(FmsfApiClient.class.getName());
    public static void main(String[] args) throws InterruptedException {
        ApiClient client = new ApiClient();
        client.setBasePath("https://fmsf2.firenet.gov/api");
        client.setUsername("<user-name>);
                client.setPassword("<api-key>");
        ResourceControllerApi resourceControllerApi = new ResourceControllerApi(client);
        ModelControllerApi modelControllerApi = new ModelControllerApi(client);

        File landscapeFile = new File("<absolute-path-to-geotiff-file-folder>/BoiseLandscape.zip");
        geotiffResource = resourceControllerApi.upload(ResourceMetadataDto.ResourceTypeEnum.GEOTIFF.getValue(), landscapeFile);

        Long landscapeId;
        if (geotiffResource != null && geotiffResource.getId() != null) {
            landscapeId = geotiffResource.getId();
        } else {
            return;
        }
        File inputfile = new File("<absolute-path-to-input-file-folder>/flam_fm1_winds.txt");
        final ModelMetadataDto modelRun =
                modelControllerApi.uploadModel(
                        ModelMetadataDto.ModelTypeEnum.FLAM.getValue(),
                        landscapeId, inputfile, 0L, 0L, 0L, 0L);
        LOGGER.log(Level.INFO, "ModelRun: " + modelRun);

        if (modelRun != null && modelRun.getRunId() != null) {
            Long modelRunId = modelRun.getRunId();
            final Set<ModelMetadataDto.RunStatusEnum> runningStatuses = Set.of(ModelMetadataDto.RunStatusEnum.INIT, ModelMetadataDto.RunStatusEnum.QUED, ModelMetadataDto.RunStatusEnum.RUN);
            LOGGER.log(Level.INFO, "Wait for model run '" + modelRunId + "' to finish...");
            int retries = 20;
            ModelMetadataDto statusModelRun;
            do {
                statusModelRun = modelControllerApi.getModelMetadata(modelRunId);
                LOGGER.log(Level.INFO, "ModelRun Status: " + statusModelRun.getRunStatus());
                Thread.onSpinWait();
                Thread.sleep(10000);
            } while (--retries > 0 || runningStatuses.contains(statusModelRun.getRunStatus()));
            LOGGER.log(Level.INFO, "Finished");
        }
    }
}
```