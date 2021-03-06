package org.carlspring.strongbox.storage.validation.artifactid;

import org.carlspring.strongbox.artifact.coordinates.ArtifactCoordinates;
import org.carlspring.strongbox.config.Maven2LayoutProviderTestConfig;
import org.carlspring.strongbox.providers.io.RepositoryFiles;
import org.carlspring.strongbox.providers.io.RepositoryPath;
import org.carlspring.strongbox.storage.repository.Repository;
import org.carlspring.strongbox.storage.validation.artifact.LowercaseValidationException;
import org.carlspring.strongbox.testing.artifact.ArtifactManagementTestExecutionListener;
import org.carlspring.strongbox.testing.artifact.MavenTestArtifact;
import org.carlspring.strongbox.testing.repository.MavenRepository;
import org.carlspring.strongbox.testing.storage.repository.RepositoryManagementTestExecutionListener;

import javax.inject.Inject;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;

/**
 * Created by dinesh on 12/10/17.
 * @author Pablo Tirado
 */
@SpringBootTest
@ActiveProfiles(profiles = "test")
@ContextConfiguration(classes = Maven2LayoutProviderTestConfig.class)
@Execution(CONCURRENT)
public class MavenArtifactIdLowercaseValidatorTest
{

    private static final String REPOSITORY_RELEASES = "mailvt-releases";

    private static final String GROUP_ID = "org.carlspring.maven.is.uppercase";

    private static final String ARTIFACT_ID = "my-maven-ARTIFACT";

    @Inject
    private MavenArtifactIdLowercaseValidator mavenArtifactIdLowercaseValidator;

    @ExtendWith({ RepositoryManagementTestExecutionListener.class,
                  ArtifactManagementTestExecutionListener.class })
    @Test
    public void validateGroupIdCase(@MavenRepository(repositoryId = REPOSITORY_RELEASES)
                                    Repository repository,
                                    @MavenTestArtifact(repositoryId = REPOSITORY_RELEASES,
                                                       id = GROUP_ID + ":" + ARTIFACT_ID,
                                                       versions = { "1.0" })
                                    Path path)
            throws Exception
    {
        RepositoryPath repositoryPath = (RepositoryPath) path.normalize();
        ArtifactCoordinates coordinates = RepositoryFiles.readCoordinates(repositoryPath);
        assertThatExceptionOfType(LowercaseValidationException.class)
                .isThrownBy(() -> mavenArtifactIdLowercaseValidator.validate(repository, coordinates));
    }

}
