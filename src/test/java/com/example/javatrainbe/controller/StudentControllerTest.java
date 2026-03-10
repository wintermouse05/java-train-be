package com.example.javatrainbe.controller;

import com.example.javatrainbe.dto.StudentRequestDto;
import com.example.javatrainbe.dto.StudentResponseDto;
import com.example.javatrainbe.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Nested
    @DisplayName("GET /api/students")
    class GetAllStudents {

        @Test
        @DisplayName("Trả về 200 và danh sách student")
        void shouldReturnListOfStudents() throws Exception {
            StudentResponseDto dto1 = new StudentResponseDto(1, "Nguyễn Văn A", "STU001", "Hà Nội", 8.5, LocalDate.of(2000, 1, 15));
            StudentResponseDto dto2 = new StudentResponseDto(2, "Trần Thị B", "STU002", "TP.HCM", 9.0, LocalDate.of(2001, 5, 20));
            when(studentService.getAllStudents()).thenReturn(List.of(dto1, dto2));

            mockMvc.perform(get("/api/students"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data.length()").value(2))
                    .andExpect(jsonPath("$.data[0].studentName").value("Nguyễn Văn A"))
                    .andExpect(jsonPath("$.data[1].studentName").value("Trần Thị B"));

            verify(studentService).getAllStudents();
        }
    }

    @Nested
    @DisplayName("GET /api/students/{id}")
    class GetStudentById {

        @Test
        @DisplayName("Trả về 200 và student khi tìm thấy")
        void shouldReturnStudent() throws Exception {
            StudentResponseDto dto = new StudentResponseDto(1, "Nguyễn Văn A", "STU001", "Hà Nội", 8.5, LocalDate.of(2000, 1, 15));
            when(studentService.getStudentById(1)).thenReturn(dto);

            mockMvc.perform(get("/api/students/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.data.studentId").value(1))
                    .andExpect(jsonPath("$.data.studentName").value("Nguyễn Văn A"))
                    .andExpect(jsonPath("$.data.studentCode").value("STU001"));

            verify(studentService).getStudentById(1);
        }
    }

    @Nested
    @DisplayName("POST /api/students")
    class CreateStudent {

        @Test
        @DisplayName("Trả về 200 khi tạo student thành công")
        void shouldCreateStudent() throws Exception {
            StudentRequestDto request = new StudentRequestDto("Nguyễn Văn A", "STU001", "Hà Nội", 8.5, LocalDate.of(2000, 1, 15));
            StudentResponseDto response = new StudentResponseDto(1, "Nguyễn Văn A", "STU001", "Hà Nội", 8.5, LocalDate.of(2000, 1, 15));
            when(studentService.createStudent(any(StudentRequestDto.class))).thenReturn(response);

            mockMvc.perform(post("/api/students")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.data.studentId").value(1))
                    .andExpect(jsonPath("$.data.studentName").value("Nguyễn Văn A"));

            verify(studentService).createStudent(any(StudentRequestDto.class));
        }
    }

    @Nested
    @DisplayName("PUT /api/students/{id}")
    class UpdateStudent {

        @Test
        @DisplayName("Trả về 200 khi cập nhật student thành công")
        void shouldUpdateStudent() throws Exception {
            StudentRequestDto request = new StudentRequestDto("Nguyễn Văn B", "STU001", "TP.HCM", 9.0, LocalDate.of(2000, 1, 15));
            StudentResponseDto response = new StudentResponseDto(1, "Nguyễn Văn B", "STU001", "TP.HCM", 9.0, LocalDate.of(2000, 1, 15));
            when(studentService.updateStudent(eq(1), any(StudentRequestDto.class))).thenReturn(response);

            mockMvc.perform(put("/api/students/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.data.studentName").value("Nguyễn Văn B"));

            verify(studentService).updateStudent(eq(1), any(StudentRequestDto.class));
        }
    }

    @Nested
    @DisplayName("DELETE /api/students/{id}")
    class DeleteStudent {

        @Test
        @DisplayName("Trả về 200 khi xóa student thành công")
        void shouldDeleteStudent() throws Exception {
            doNothing().when(studentService).deleteStudent(1);

            mockMvc.perform(delete("/api/students/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.message").value("Xóa sinh viên thành công"));

            verify(studentService).deleteStudent(1);
        }
    }
}
