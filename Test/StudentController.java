@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @PostMapping
    public ResponseEntity<String> addStudent(@RequestBody Student student) {
        Optional<Student> existingStudent = studentRepository.findByRollNo(student.getRollNo());
        if (existingStudent.isPresent()) {
            return ResponseEntity.badRequest().body("Student already exists");
        }
        
        studentRepository.save(student);
        return ResponseEntity.ok("Student added successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> editStudent(@PathVariable Long id, @RequestBody Student student) {
        Optional<Student> existingStudent = studentRepository.findById(id);
        if (existingStudent.isPresent()) {
            Student updatedStudent = existingStudent.get();
            updatedStudent.setName(student.getName());
            updatedStudent.setDepartment(student.getDepartment());
            updatedStudent.setStandard(student.getStandard());
            updatedStudent.setGender(student.getGender());
            updatedStudent.setAge(student.getAge());
            studentRepository.save(updatedStudent);
            return ResponseEntity.ok("Student updated successfully");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Optional<Student> student = studentRepository.findById(id);
        return student.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<Student>> getAllStudents(Pageable pageable) {
        Page<Student> students = studentRepository.findAll(pageable);
        return ResponseEntity.ok(students);
    }
}
