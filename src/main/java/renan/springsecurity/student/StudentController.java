package renan.springsecurity.student;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
/*Combinação das anotações @Controller e @ResponseBody. Usada para marcar que o
controlador está fornecendo serviços REST com o tipo da resposta JSON/XML.*/
@RestController
//Usada para mapear solicitações da 'web' para classes manipuladoras específicas e métodos manipuladores.
@RequestMapping("/students")
public class StudentController {
//Lista criada para instanciar e mocar dados dentro da classe 'Student'.
    private static final List<Student> students = Arrays.asList(
            new Student(1, "Renan"),
            new Student(2, "Fernanda")
    );
//Método para pegar cada student, dentro da lista 'students' e encontrar através da request "GET".
    @GetMapping(path = "{studentId}")
    public Student getStudent(@PathVariable("studentId") Integer studentId) {
        return students.stream().filter(student -> studentId.equals(student.getStudentId()))
                .findFirst().orElseThrow(() -> new IllegalStateException("Student" + studentId + "is not valid"));
    }
}
