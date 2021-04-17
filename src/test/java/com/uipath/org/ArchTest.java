package com.uipath.org;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.uipath.org");

        noClasses()
            .that()
            .resideInAnyPackage("com.uipath.org.service..")
            .or()
            .resideInAnyPackage("com.uipath.org.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.uipath.org.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
