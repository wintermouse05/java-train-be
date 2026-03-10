package com.example.javatrainbe.service;

import com.example.javatrainbe.dao.StudentDao;
import com.example.javatrainbe.dao.StudentInfoDao;
import com.example.javatrainbe.dto.StudentRequestDto;
import com.example.javatrainbe.dto.StudentResponseDto;
import com.example.javatrainbe.entity.Student;
import com.example.javatrainbe.entity.StudentInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.seasar.doma.jdbc.Result;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentDao studentDao;

    @Mock
    private StudentInfoDao studentInfoDao;

    @InjectMocks
    private StudentService studentService;

    private Student student;
    private StudentInfo studentInfo;
    private StudentRequestDto requestDto;

    @BeforeEach
    void setUp() {
        student = new Student(1, "Nguyễn Văn A", "STU001");
        studentInfo = new StudentInfo(1, 1, "Hà Nội", 8.5, LocalDate.of(2000, 1, 15));
        requestDto = new StudentRequestDto("Nguyễn Văn A", "STU001", "Hà Nội", 8.5, LocalDate.of(2000, 1, 15));
    }

    @Nested
    @DisplayName("getAllStudents")
    class GetAllStudents {

        @Test
        @DisplayName("Trả về danh sách student khi có dữ liệu")
        void shouldReturnListOfStudents() {
            Student student2 = new Student(2, "Trần Thị B", "STU002");
            StudentInfo studentInfo2 = new StudentInfo(2, 2, "TP.HCM", 9.0, LocalDate.of(2001, 5, 20));

            when(studentDao.selectAll()).thenReturn(List.of(student, student2));
            when(studentInfoDao.findByStudentId(1)).thenReturn(Optional.of(studentInfo));
            when(studentInfoDao.findByStudentId(2)).thenReturn(Optional.of(studentInfo2));

            List<StudentResponseDto> result = studentService.getAllStudents();

            assertEquals(2, result.size());
            assertEquals("Nguyễn Văn A", result.get(0).getStudentName());
            assertEquals("Trần Thị B", result.get(1).getStudentName());
            assertEquals(8.5, result.get(0).getAverageScore());
            verify(studentDao).selectAll();
        }

        @Test
        @DisplayName("Trả về danh sách rỗng khi không có dữ liệu")
        void shouldReturnEmptyListWhenNoStudents() {
            when(studentDao.selectAll()).thenReturn(Collections.emptyList());

            List<StudentResponseDto> result = studentService.getAllStudents();

            assertTrue(result.isEmpty());
            verify(studentDao).selectAll();
        }
    }

    @Nested
    @DisplayName("getStudentById")
    class GetStudentById {

        @Test
        @DisplayName("Trả về student khi tìm thấy")
        void shouldReturnStudentWhenFound() {
            when(studentDao.selectById(1)).thenReturn(student);
            when(studentInfoDao.findByStudentId(1)).thenReturn(Optional.of(studentInfo));

            StudentResponseDto result = studentService.getStudentById(1);

            assertEquals(1, result.getStudentId());
            assertEquals("Nguyễn Văn A", result.getStudentName());
            assertEquals("STU001", result.getStudentCode());
            assertEquals("Hà Nội", result.getAddress());
            assertEquals(8.5, result.getAverageScore());
        }

        @Test
        @DisplayName("Trả về student không có info khi StudentInfo chưa có")
        void shouldReturnStudentWithoutInfoWhenStudentInfoNotFound() {
            when(studentDao.selectById(1)).thenReturn(student);
            when(studentInfoDao.findByStudentId(1)).thenReturn(Optional.empty());

            StudentResponseDto result = studentService.getStudentById(1);

            assertEquals(1, result.getStudentId());
            assertNull(result.getAddress());
            assertNull(result.getAverageScore());
        }

        @Test
        @DisplayName("Ném exception khi student không tồn tại")
        void shouldThrowExceptionWhenStudentNotFound() {
            when(studentDao.selectById(99)).thenReturn(null);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> studentService.getStudentById(99));
            assertEquals("Student không tồn tại với ID: 99", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("createStudent")
    class CreateStudent {

        @Test
        @DisplayName("Tạo student thành công")
        void shouldCreateStudentSuccessfully() {
            when(studentDao.findByStudentCode("STU001")).thenReturn(Optional.empty());

            @SuppressWarnings("unchecked")
            Result<Student> studentResult = mock(Result.class);
            when(studentResult.getEntity()).thenReturn(student);
            when(studentDao.insert(any(Student.class))).thenReturn(studentResult);

            @SuppressWarnings("unchecked")
            Result<StudentInfo> infoResult = mock(Result.class);
            when(studentInfoDao.insert(any(StudentInfo.class))).thenReturn(infoResult);

            when(studentInfoDao.findByStudentId(1)).thenReturn(Optional.of(studentInfo));

            StudentResponseDto result = studentService.createStudent(requestDto);

            assertEquals("Nguyễn Văn A", result.getStudentName());
            assertEquals("STU001", result.getStudentCode());
            verify(studentDao).insert(any(Student.class));
            verify(studentInfoDao).insert(any(StudentInfo.class));
        }

        @Test
        @DisplayName("Ném exception khi mã sinh viên đã tồn tại")
        void shouldThrowExceptionWhenStudentCodeExists() {
            when(studentDao.findByStudentCode("STU001")).thenReturn(Optional.of(student));

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> studentService.createStudent(requestDto));
            assertEquals("Mã sinh viên đã tồn tại: STU001", exception.getMessage());
            verify(studentDao, never()).insert(any());
        }
    }

    @Nested
    @DisplayName("updateStudent")
    class UpdateStudent {

        @Test
        @DisplayName("Cập nhật student thành công khi đã có StudentInfo")
        void shouldUpdateStudentWithExistingInfo() {
            Student updatedStudent = new Student(1, "Nguyễn Văn B", "STU001");

            when(studentDao.selectById(1)).thenReturn(student);

            @SuppressWarnings("unchecked")
            Result<Student> studentResult = mock(Result.class);
            when(studentDao.update(any(Student.class))).thenReturn(studentResult);

            when(studentInfoDao.findByStudentId(1))
                    .thenReturn(Optional.of(studentInfo))   // for update check
                    .thenReturn(Optional.of(studentInfo));   // for convertToResponse

            @SuppressWarnings("unchecked")
            Result<StudentInfo> infoResult = mock(Result.class);
            when(studentInfoDao.update(any(StudentInfo.class))).thenReturn(infoResult);

            StudentRequestDto updateDto = new StudentRequestDto("Nguyễn Văn B", "STU001", "TP.HCM", 9.0, LocalDate.of(2000, 1, 15));
            StudentResponseDto result = studentService.updateStudent(1, updateDto);

            assertEquals("Nguyễn Văn B", result.getStudentName());
            verify(studentDao).update(any(Student.class));
            verify(studentInfoDao).update(any(StudentInfo.class));
        }

        @Test
        @DisplayName("Cập nhật student và tạo mới StudentInfo nếu chưa có")
        void shouldUpdateStudentAndCreateInfoIfNotExists() {
            when(studentDao.selectById(1)).thenReturn(student);

            @SuppressWarnings("unchecked")
            Result<Student> studentResult = mock(Result.class);
            when(studentDao.update(any(Student.class))).thenReturn(studentResult);

            when(studentInfoDao.findByStudentId(1))
                    .thenReturn(Optional.empty())            // for update check — no info
                    .thenReturn(Optional.of(studentInfo));   // for convertToResponse

            @SuppressWarnings("unchecked")
            Result<StudentInfo> infoResult = mock(Result.class);
            when(studentInfoDao.insert(any(StudentInfo.class))).thenReturn(infoResult);

            StudentResponseDto result = studentService.updateStudent(1, requestDto);

            assertNotNull(result);
            verify(studentInfoDao).insert(any(StudentInfo.class));
            verify(studentInfoDao, never()).update(any(StudentInfo.class));
        }

        @Test
        @DisplayName("Ném exception khi student không tồn tại")
        void shouldThrowExceptionWhenStudentNotFound() {
            when(studentDao.selectById(99)).thenReturn(null);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> studentService.updateStudent(99, requestDto));
            assertEquals("Student không tồn tại với ID: 99", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("deleteStudent")
    class DeleteStudent {

        @Test
        @DisplayName("Xóa student thành công khi có StudentInfo")
        void shouldDeleteStudentWithInfo() {
            when(studentDao.selectById(1)).thenReturn(student);
            when(studentInfoDao.findByStudentId(1)).thenReturn(Optional.of(studentInfo));

            @SuppressWarnings("unchecked")
            Result<StudentInfo> infoResult = mock(Result.class);
            when(studentInfoDao.delete(studentInfo)).thenReturn(infoResult);

            @SuppressWarnings("unchecked")
            Result<Student> studentResult = mock(Result.class);
            when(studentDao.delete(student)).thenReturn(studentResult);

            studentService.deleteStudent(1);

            verify(studentInfoDao).delete(studentInfo);
            verify(studentDao).delete(student);
        }

        @Test
        @DisplayName("Xóa student thành công khi không có StudentInfo")
        void shouldDeleteStudentWithoutInfo() {
            when(studentDao.selectById(1)).thenReturn(student);
            when(studentInfoDao.findByStudentId(1)).thenReturn(Optional.empty());

            @SuppressWarnings("unchecked")
            Result<Student> studentResult = mock(Result.class);
            when(studentDao.delete(student)).thenReturn(studentResult);

            studentService.deleteStudent(1);

            verify(studentInfoDao, never()).delete(any());
            verify(studentDao).delete(student);
        }

        @Test
        @DisplayName("Ném exception khi student không tồn tại")
        void shouldThrowExceptionWhenStudentNotFound() {
            when(studentDao.selectById(99)).thenReturn(null);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> studentService.deleteStudent(99));
            assertEquals("Student không tồn tại với ID: 99", exception.getMessage());
        }
    }
}
