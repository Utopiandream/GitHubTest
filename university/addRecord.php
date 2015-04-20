<?php
if (isset($_GET['mn'])) {
    $mn = intval($_GET['mn']);
} else {
    $mn = 0;
}

$dbhost = "localhost";
$dbuser = "root";
$dbpassword = "";
$dbname = "university";

$con = mysql_connect($dbhost, $dbuser, $dbpassword);

if (!$con) {
    die('Could not connect: ' . mysql_error());
}

mysql_select_db($dbname, $con);

if ($mn == 0) {
    if (isset($_POST['student_number'])) {
        $student_number = intval ($_POST['student_number']);
    } else {
        $student_number = 0;
    }

    if (isset($_POST['student_name'])) {
        $student_name = $_POST['student_name'];
    } else {
        $student_name = "";
    }

    if (isset($_POST['student_myclass'])) {
        $student_myclass = intval ($_POST['student_myclass']);
    } else {
        $student_myclass = 0;
    }

    if (isset($_POST['student_major'])) {
        $student_major = $_POST['student_major'];
    } else {
        $student_major = "";
    }

    if ($student_number > 0 && strlen($student_name) > 0 && $student_myclass > 0 && strlen($student_major) > 0) {
        $fields = "(student_number, name, myclass, major)";
        $values = "($student_number, '$student_name', $student_myclass, '$student_major')";

        $query = "INSERT INTO student " . $fields . " VALUES " . $values;
        mysql_query($query);
    }
} else if ($mn == 1) {
    if (isset($_POST['course_number'])) {
        $course_number = $_POST['course_number'];
    } else {
        $course_number = "";
    }

    if (isset($_POST['course_name'])) {
        $course_name = $_POST['course_name'];
    } else {
        $course_name = "";
    }

    if (isset($_POST['course_credit_hours'])) {
        $course_credit_hours = intval ($_POST['course_credit_hours']);
    } else {
        $projloccourse_credit_hours = 0;
    }

    if (isset($_POST['course_department'])) {
        $course_department = $_POST['course_department'];
    } else {
        $course_department = "";
    }

    if (strlen($course_number) > 0 && strlen($course_name) > 0 && $course_credit_hours > 0 && strlen($course_department) > 0) {
        $fields = "(course_number, course_name, credit_hours, department)";
        $values = "('$course_number', '$course_name', $course_credit_hours, '$course_department')";

        $query = "INSERT INTO course " . $fields . " VALUES " . $values;
        mysql_query($query);
    }
} else if ($mn == 2) {
    if (isset($_POST['section_identifier'])) {
        $section_identifier = intval ($_POST['section_identifier']);
    } else {
        $section_identifier = 0;
    }

    if (isset($_POST['section_course_number'])) {
        $section_course_number = $_POST['section_course_number'];
    } else {
        $section_course_number = "";
    }

    if (isset($_POST['section_semester'])) {
        $section_semester = $_POST['section_semester'];
    } else {
        $section_semester = "";
    }

    if (isset($_POST['section_myyear'])) {
        $section_myyear = $_POST['section_myyear'];
    } else {
        $section_myyear = "";
    }
    
    if (isset($_POST['section_instructor'])) {
        $section_instructor = $_POST['section_instructor'];
    } else {
        $section_instructor = "";
    }
    
    if ($section_identifier > 0 && strlen($section_course_number) > 0 && strlen($section_semester) > 0 && strlen($section_myyear) > 0 && strlen($section_instructor)) {
        $fields = "(section_identifier, course_number, semester, myyear, instructor)";
        $values = "($section_identifier, '$section_course_number', '$section_semester', '$section_myyear', '$section_instructor')";

        $query = "INSERT INTO mysection " . $fields . " VALUES " . $values;
        mysql_query($query);
    }
} else if ($mn == 3) {
    if (isset($_POST['grade_student_number'])) {
        $grade_student_number = intval ($_POST['grade_student_number']);
    } else {
        $grade_student_number = 0;
    }

    if (isset($_POST['grade_section_identifier'])) {
        $grade_section_identifier = intval ($_POST['grade_section_identifier']);
    } else {
        $grade_section_identifier = 0;
    }

    if (isset($_POST['grade_grade'])) {
        $grade_grade = $_POST['grade_grade'];
    } else {
        $grade_grade = "";
    }

    if ($grade_student_number > 0 && $grade_section_identifier > 0 && strlen($grade_grade) > 0) {
        $fields = "(student_number, section_identifier, grade)";
        $values = "($grade_student_number, $grade_section_identifier, '$grade_grade')";

        $query = "INSERT INTO grade_report " . $fields . " VALUES " . $values;
        mysql_query($query);
    }
} else if ($mn == 4) {
    if (isset($_POST['prereq_course_number'])) {
        $prereq_course_number = $_POST['prereq_course_number'];
    } else {
        $prereq_course_number = "";
    }

    if (isset($_POST['prereq_number'])) {
        $prereq_number = $_POST['prereq_number'];
    } else {
        $prereq_number = "";
    }


    if (strlen($prereq_course_number) > 0 && strlen($prereq_number)) {
        $fields = "(course_number, prerequisite_number)";
        $values = "('$prereq_course_number', '$prereq_number')";

        $query = "INSERT INTO prerequisite " . $fields . " VALUES " . $values;
        mysql_query($query);
    }
}


mysql_close($con);

header('Location: index1.php?mn=' . $mn);
?>
