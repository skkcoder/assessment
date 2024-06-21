package com.gift.go.assessment.fileprocessing.validation

import com.gift.go.assessment.fileprocessing.services.IPInformationService
import org.springframework.stereotype.Component

@Component
class FileProcessorValidator(val ipInformationService: IPInformationService) {

    fun validate() {

    }
}