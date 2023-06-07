@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherRepository teacherRepository;

    public TeacherController(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUpTeacher(@RequestBody Teacher teacher) {
        Optional<Teacher> existingTeacher = teacherRepository.findByEmail(teacher.getEmail());
        if (existingTeacher.isPresent()) {
            return ResponseEntity.badRequest().body("Teacher already exists");
        }
        
     
        String encryptedPassword = encryptPassword(teacher.getPassword());
        teacher.setPassword(encryptedPassword);

        teacherRepository.save(teacher);
        return ResponseEntity.ok("Teacher signed up successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginTeacher(@RequestBody Teacher teacher) {
        Optional<Teacher> existingTeacher = teacherRepository.findByEmail(teacher.getEmail());
        if (existingTeacher.isPresent()) {
            // Decrypt and compare the provided password with the stored password
            if (decryptPassword(teacher.getPassword()).equals(existingTeacher.get().getPassword())) {
                return ResponseEntity.ok("Teacher logged in successfully");
            }
        }
        return ResponseEntity.badRequest().body("Invalid credentials");
    }
}
