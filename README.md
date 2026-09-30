# Student Management System — AOOP Assignment

## Run

```bash
./mvnw spring-boot:run
```

The API is available at `http://localhost:8080/api/students`.

## Postman endpoints

Use `Content-Type: application/json` for POST requests.

| Method | URL | Purpose |
|---|---|---|
| POST | `/api/students` | Add a student |
| GET | `/api/students` | View all students; service uses an `Iterator` |
| GET | `/api/students/{id}` | View one student by ID |
| GET | `/api/students/sort/id` | Sort by ID using `Comparable` |
| GET | `/api/students/sort/cgpa` | Sort by CGPA descending using `Comparator` |
| GET | `/api/students/departments` | Return unique departments using `HashSet` |
| GET | `/api/students/recent` | View the stack's last three students |
| GET | `/api/students/recent/peek` | Read the most recently added student without removing it |
| DELETE | `/api/students/recent/pop` | Remove and return the most recently added student |
| POST | `/api/students/save` | Serialize students to `students.ser` |
| POST | `/api/students/load` | Deserialize students from `students.ser` |

Example request body:

```json
{
  "id": 101,
  "name": "Ayesha Rahman",
  "department": "CSE",
  "cgpa": 3.85
}
```

## Collection and I/O notes

Students are stored in an `ArrayList`. `Student` implements `Comparable<Student>` and compares IDs. `CgpaDescendingComparator` compares CGPAs in descending order. A `HashSet<String>` removes duplicate department names, and a `Stack<Student>` keeps only the last three additions for `push`, `peek`, and `pop` operations.

Java byte streams (`InputStream`/`OutputStream`) read and write raw binary bytes, so `ObjectInputStream` and `ObjectOutputStream` are appropriate for serialization of `Student` objects. Character streams (`Reader`/`Writer`), such as `FileReader` and `FileWriter`, read and write text characters and are appropriate for text files such as CSV or JSON. This project uses buffered byte streams around object streams to save and restore the serialized student list.
