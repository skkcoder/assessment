package com.gift.go.assessment.fileprocessing.api

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class FileProcessorControllerTest {


//    @Test
//    fun testProcessFile() {
//        val mockFile = MockMultipartFile("file", "Hello, World!".toByteArray())
//
//        mockMvc.perform(
//            multipart("/api/files")
//                .file(mockFile)
//                .contentType(MediaType.MULTIPART_FORM_DATA)
//        )
//            .andExpect(status().isOk)
//    }
}