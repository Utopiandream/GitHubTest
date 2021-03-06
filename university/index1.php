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
//Student: number, name, myclass, major
if ($mn == 0) {
    $student_numberArr = array();
    $student_nameArr = array();
    $student_myclassArr = array();
    $student_majorArr = array();

    $student_2D = array();

    $query = "SELECT * FROM student";
    $result = mysql_query($query);

    if ($result) {
        while ($row = mysql_fetch_assoc($result)) {
            $student_numberArr[] = $row['student_number'];
            $student_nameArr[] = $row['name'];
            $student_myclassArr[] = $row['myclass'];
            $student_majorArr[] = $row['major'];
        }
    }
    for ($j = 0; $j < sizeof($student_nameArr); $j++) {

        $student_2D[$j][0] = $student_numberArr[$j];
        $student_2D[$j][1] = $student_nameArr[$j];
        $student_2D[$j][2] = $student_myclassArr[$j];
        $student_2D[$j][3] = $student_majorArr[$j];
    }
} //Course: course_number, course_name, credit_hours, department
else if ($mn == 1) {
    $course_numberArr = array();
    $course_nameArr = array();
    $course_credit_hoursArr = array();
    $course_departmentArr = array();
    $course_2D = array();


    $query = "SELECT * FROM course";
    $result = mysql_query($query);

    if ($result) {
        while ($row = mysql_fetch_assoc($result)) {
            $course_numberArr[] = $row['course_number'];
            $course_nameArr[] = $row['course_name'];
            $course_credit_hoursArr[] = $row['credit_hours'];
            $course_departmentArr[] = $row['department'];
        }
    }
    
    for ($j = 0; $j < sizeof($course_numberArr); $j++) {

        $course_2D[$j][0] = $course_numberArr[$j];
        $course_2D[$j][1] = $course_nameArr[$j];
        $course_2D[$j][2] = $course_credit_hoursArr[$j];
        $course_2D[$j][3] = $course_departmentArr[$j];
    }
} // Section: section_identifier, course_number, semester, myyear, instructor
else if ($mn == 2) {
    $section_identifierArr = array();
    $section_course_numberArr = array();
    $section_semesterArr = array();
    $section_myyearArr = array();
    $section_instructor = array();

    $query = "SELECT * FROM mysection";
    $result = mysql_query($query);

    if ($result) {
        while ($row = mysql_fetch_assoc($result)) {
            $section_identifierArr[] = $row['section_identifier'];
            $section_course_numberArr[] = $row['course_number'];
            $section_semesterArr[] = $row['semester'];
            $section_myyearArr[] = $row['myyear'];
            $section_instructor[] = $row['instructor'];
        }
    }
    //Grade Report student_number, section_identifier, grade
} else if ($mn == 3) {
    $grade_student_numberArr = array();
    $grade_section_identifierArr = array();
    $grade_gradeArr = array();

    $query = "SELECT * FROM grade_report";
    $result = mysql_query($query);

    if ($result) {
        while ($row = mysql_fetch_assoc($result)) {
            $grade_student_numberArr[] = $row['student_number'];
            $grade_section_identifierArr[] = $row['section_identifier'];
            $grade_gradeArr[] = $row['grade'];
        }
    }
} //Prerequisite: course_number, prerequisite_number
else if ($mn == 4) {
    $prereq_course_numberArr = array();
    $prereq_numberArr = array();

    $query = "SELECT * FROM prerequisite";
    $result = mysql_query($query);

    if ($result) {
        while ($row = mysql_fetch_assoc($result)) {
            $prereq_course_numberArr[] = $row['course_number'];
            $prereq_numberArr[] = $row['prerequisite_number'];
        }
    }
} else if ($mn == 5) {


    //Get The name of the Students
    $trans_student_name = array();
    $query = "SELECT * FROM student";
    $result = mysql_query($query);
    if ($result) {
        while ($row = mysql_fetch_assoc($result)) {
            $trans_student_name[] = $row['name'];
        }
    }

    //find student courses attached to each student number
    $trans_student_INFO[][] = array();
    $trans_student_grade[][] = array();
    $trans_student_semester[][] = array();
    $trans_student_year[][] = array();
    $trans_student_sectionID[] [] = array();
    for ($j = 0; $j < count($trans_student_name); $j++) {
        $query = "SELECT * FROM grade_report as g, mysection as m, student as s
WHERE s.student_number = g.student_number AND g.section_identifier = m.section_identifier AND s.name = '$trans_student_name[$j]'";
        $result = mysql_query($query);

        if ($result) {
            while ($row = mysql_fetch_assoc($result)) {
                $trans_student_INFO[$j][] = $row['course_number'];
                $trans_student_grade[$j][] = $row['grade'];
                $trans_student_semester[$j][] = $row['semester'];
                $trans_student_year[$j][] = $row['myyear'];
                $trans_student_sectionID[$j][] = $row['section_identifier'];
            }
        }
    }
}
?>
<html>
    <head>
        <meta charset="UTF-8">
        <title>University: Project1</title>
    </head>
    <script>
        var xmlhttp;

        function loadXMLDoc(url, cfunc) { // Connection using ajax
            if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
                xmlhttp = new XMLHttpRequest();
            }
            else {// code for IE6, IE5
                xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
            }

            xmlhttp.onreadystatechange = cfunc;
            xmlhttp.open("GET", url, true);
            xmlhttp.send();
        }



        function editfieldAjax(v, rn, cols) {
            var valArr = "";
        for (j = 0; j < cols; j++) {
                valArr = valArr + document.getElementById("name" + rn + j).innerHTML + "~";}
            var myurl = "ajax/editfieldAjax.php?id="+ v + "&rn=" + rn + "&mn=" + <?php print $mn; ?> +"&val=" + valArr;

            loadXMLDoc(myurl, function () {
                if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
                    result = xmlhttp.responseText;
                    var myArr = result.split("~");
                    
                     for (i = 0; i < cols; i ++) { 
                     document.getElementById("name" + rn + i).innerHTML = "<input id=\"new" + rn + "~" + i + "\" type=\"text\" value=\"" + myArr[i] + "\" />";
                     }
                    document.getElementById("studentbtn" + v).innerHTML = "<input type=\"button\" onclick=\"updatefieldAjax(" +"'"+ v +"'" + "," + rn + "," + cols +")\" value=\"Update\" />";
                }
            });
        }






        function updatefieldAjax(v, rn, cols) {
            var valArr = "";
        for (j = 0; j < cols; j++) {
                valArr = valArr + document.getElementById("new" + rn + "~" + j).value + "~";}
            var myurl = "ajax/updatefieldAjax.php?id=" + v + "&rn=" + rn + "&mn=" + <?php print $mn; ?> +"&val=" + valArr;


            loadXMLDoc(myurl, function () {
                if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
                    result = xmlhttp.responseText;
                    var myArr2 = result.split("~");
    for (i = 0; i < 4; i ++) {
                            document.getElementById("name" + rn + i).innerHTML = myArr2[i];
     }
                    document.getElementById("studentbtn" + v).innerHTML = "<input type=\"button\" onclick=\"editfieldAjax(" +"'"+ v +"'" + "," + rn + "," + cols +")\" value=\"Edit\" />";
                }
            });
        }

    </script>
    <body>
        <?php if ($mn <= 4) {
            ?>
            <table>
                <tr>
                    <th>Home</th>
                    <th><a href="index1.php?mn=5">Transcript</a></th>
                </tr><tr><td colspan="5"><hr /></td></tr>

                <tr>
                    <?php
                    if ($mn == 0) {
                        ?>

                        <th style="width: 7em">Student</th>
                        <th style="width: 7em">
                            <a href="index1.php?mn=1">Course</a>
                        </th>
                        <th style="width: 7em">
                            <a href="index1.php?mn=2">Section</a>
                        </th>
                        <th style="width: 7em">
                            <a href="index1.php?mn=3">Grade Report</a>
                        </th>
                        <th style="width: 7em">
                            <a href="index1.php?mn=4">Prerequisite</a>
                        </th>
                        <?php
                    } else if ($mn == 1) {
                        ?>
                        <th style="width: 7em">
                            <a href="index1.php?mn=0">Student</a>
                        </th>
                        <th style="width: 7em">Course</th>
                        <th style="width: 7em">
                            <a href="index1.php?mn=2">Section</a>
                        </th>
                        <th style="width: 7em">
                            <a href="index1.php?mn=3">Grade Report</a>
                        </th>
                        <th style="width: 7em">
                            <a href="index1.php?mn=4">Prerequisite</a>
                        </th>
                        <?php
                    } else if ($mn == 2) {
                        ?>
                        <th style="width: 7em">
                            <a href="index1.php?mn=0">Student</a>
                        </th>
                        <th style="width: 7em">
                            <a href="index1.php?mn=1">Course</a>
                        </th>
                        <th style="width: 7em">Section</th>
                        <th style="width: 7em">
                            <a href="index1.php?mn=3">Grade Report</a>
                        </th>
                        <th style="width: 7em">
                            <a href="index1.php?mn=4">Prerequisite</a>
                        </th>
                        <?php
                    } else if ($mn == 2) {
                        ?>

                        <?php
                    } else if ($mn == 3) {
                        ?>
                        <th style="width: 7em">
                            <a href="index1.php?mn=0">Student</a>
                        </th>
                        <th style="width: 7em">
                            <a href="index1.php?mn=1">Course</a>
                        </th>
                        <th style="width: 7em">
                            <a href="index1.php?mn=2">Section</a>
                        </th>
                        <th style="width: 7em">Grade Report</th>
                        <th style="width: 7em">
                            <a href="index1.php?mn=4">Prerequisite</a>
                        </th>
                        <?php
                    } else if ($mn == 4) {
                        ?>
                        <th style="width: 7em">
                            <a href="index1.php?mn=0">Student</a>
                        </th>
                        <th style="width: 7em">
                            <a href="index1.php?mn=1">Course</a>
                        </th>
                        <th style="width: 7em">
                            <a href="index1.php?mn=2">Section</a>
                        </th>
                        <th style="width: 7em">
                            <a href="index1.php?mn=3">Grade Report</a>
                        </th>
                        <th style="width: 7em">Prerequisite</th>
                        <?php
                    }
                    ?>
                </tr>
                <tr>
                    <td colspan="5"><hr /></td>
                </tr>
            </table>
            <form action="addRecord.php?mn=<?php print $mn; ?>" method="POST">
                <table>
                    <?php
                    //Print Column Headers
                    if ($mn == 0) {
                        ?>
                        <th style="width: 8em">student_number</th>
                        <th style="width: 8em">name</th>
                        <th style="width: 8em">myclass</th>
                        <th style="width: 8em">major</th>
                        <?php
                    } else if ($mn == 1) {
                        ?>
                        <th style="width: 8em">course_number</th>
                        <th style="width: 8em">course_name</th>
                        <th style="width: 8em">credit_hours</th>
                        <th style="width: 8em">department</th>
                        <?php
                    } else if ($mn == 2) {
                        ?>
                        <th style="width: 8em">section_identifier</th>
                        <th style="width: 8em">course_number</th>
                        <th style="width: 8em">semester</th>
                        <th style="width: 8em">myyear</th>
                        <th style="width: 8em">instructor</th>
                        <?php
                    } else if ($mn == 3) {
                        ?>
                        <th style="width: 8em">student_number</th>
                        <th style="width: 8em">section_identifier</th>
                        <th style="width: 8em">grade</th>
                        <?php
                    } else if ($mn == 4) {
                        ?>
                        <th style="width: 8em">course_number</th>
                        <th style="width: 8em">prerequisite_number</th>
                        <?php
                    }
                    //Print Data for Tables
                    if ($mn == 0) {
                        for ($j = 0; $j < count($student_numberArr); $j++) {
                            ?>
                            <tr>
                                <td id="name<?php print $j; ?>0" style="text-align: center"><?php print $student_2D[$j][0]; ?></td>
                                <td id="name<?php print $j; ?>1" style="text-align: center"><?php print $student_2D[$j][1]; ?></td>
                                <td id="name<?php print $j; ?>2" style="text-align: center"><?php print $student_2D[$j][2]; ?></td>
                                <td id="name<?php print $j; ?>3" style="text-align: center"><?php print $student_2D[$j][3]; ?></td>
                                <td id="studentbtn<?php print $student_2D[$j][0]; ?>"><input type="button" onclick="editfieldAjax(<?php print $student_2D[$j][0]; ?>,<?php print $j ?>,<?php print count($student_2D[$j])?>)" value="Edit" /></td>
                            </tr>
                            <?php
                            
                        }
                        ?>
                        <tr><td colspan="5"><hr /></td></tr>
                        <tr>
                            <td><input type="text" name="student_number" /></td>
                            <td><input type="text" name="student_name" /></td>
                            <td><input type="text" name="student_myclass" /></td>
                            <td><input type="text" name="student_major" /></td>
                        </tr>
                        <?php
                    } else if ($mn == 1) {
                        for ($j = 0; $j < count($course_numberArr); $j++) {
                            ?>
                            <tr>
                                <td id="name<?php print $j; ?>0" style="text-align: center"><?php print $course_numberArr[$j]; ?></td>
                                <td id="name<?php print $j; ?>1" style="text-align: center"><?php print $course_nameArr[$j]; ?></td>
                                <td id="name<?php print $j; ?>2" style="text-align: center"><?php print $course_credit_hoursArr[$j]; ?></td>
                                <td id="name<?php print $j; ?>3" style="text-align: center"><?php print $course_departmentArr[$j]; ?></td>
                        <td id="studentbtn<?php print $course_2D[$j][0]; ?>"><input type="button" onclick='editfieldAjax("<?php print $course_numberArr[$j]; ?>",<?php print $j ?>,<?php print count($course_2D[$j])?>)' value="Edit" /></td>
                            </tr>
                            <?php
                        }
                        ?>
                        <tr><td colspan="5"><hr /></td></tr>
                        <tr>
                            <td><input type="text" name="course_number" /></td>
                            <td><input type="text" name="course_name" /></td>
                            <td><input type="text" name="course_credit_hours" /></td>
                            <td><input type="text" name="course_department" /></td>
                        </tr>
                        <?php
                    } else if ($mn == 2) {
                        for ($j = 0; $j < count($section_identifierArr); $j++) {
                            ?>
                            <tr>
                                <td id="name<?php print $section_identifierArr[$i]; ?>" style="text-align: center"><?php print $section_identifierArr[$j]; ?></td>
                                <td id="name<?php print $section_course_numberArr[$i]; ?>" style="text-align: center"><?php print $section_course_numberArr[$j]; ?></td>
                                <td id="name<?php print $section_semesterArr[$i]; ?>" style="text-align: center"><?php print $section_semesterArr[$j]; ?></td>
                                <td id="name<?php print $section_myyearArr[$i]; ?>" style="text-align: center"><?php print $section_myyearArr[$j]; ?></td>
                                <td id="name<?php print $section_instructor[$i]; ?>" style="text-align: center"><?php print $section_instructor[$j]; ?></td>
                                <td id="studentbtn<?php print "edit"; ?>"><input type="button" onclick="editfieldAjax(<?php print "edit"; ?>)" value="Edit" /></td>
                            </tr>
                            <?php
                        }
                        ?>
                        <tr><td colspan="5"><hr /></td></tr>    
                        <tr>
                            <td><input type="text" name="section_identifier" /></td>
                            <td><input type="text" name="section_course_number" /></td>
                            <td><input type="text" name="section_semester" /></td>
                            <td><input type="text" name="section_myyear" /></td>
                            <td><input type="text" name="section_instructor" /></td>
                        </tr>
                        <?php
                    } else if ($mn == 3) {
                        for ($j = 0; $j < count($grade_student_numberArr); $j++) {
                            ?>
                            <tr>
                                <td id="name<?php print $grade_student_numberArr[$i]; ?>" style="text-align: center"><?php print $grade_student_numberArr[$j]; ?></td>
                                <td id="name<?php print $grade_section_identifierArr[$i]; ?>" style="text-align: center"><?php print $grade_section_identifierArr[$j]; ?></td>
                                <td id="name<?php print $grade_gradeArr[$i]; ?>" style="text-align: center"><?php print $grade_gradeArr[$j]; ?></td>
                                <td id="studentbtn<?php print "edit"; ?>"><input type="button" onclick="editfieldAjax(<?php print "edit"; ?>)" value="Edit" /></td>
                            </tr>
                            <?php
                        }
                        ?>
                        <tr><td colspan="5"><hr /></td></tr>    
                        <tr>
                            <td><input type="text" name="grade_student_number" /></td>
                            <td><input type="text" name="grade_section_identifier" /></td>
                            <td><input type="text" name="grade_grade" /></td>
                        </tr>
                        <?php
                    } else if ($mn == 4) {
                        for ($j = 0; $j < count($prereq_course_numberArr); $j++) {
                            ?>
                            <tr>
                                <td id="name<?php print $prereq_course_numberArr[$i]; ?>" style="text-align: center"><?php print $prereq_course_numberArr[$j]; ?></td>
                                <td id="name<?php print $prereq_numberArr[$i]; ?>" style="text-align: center"><?php print $prereq_numberArr[$j]; ?></td>
                                <td id="studentbtn<?php print "edit"; ?>"><input type="button" onclick="editfieldAjax(<?php print "edit"; ?>)" value="Edit" /></td>
                            </tr>
                            <?php
                        }
                        ?>
                        <tr><td colspan="5"><hr /></td></tr>    
                        <tr>
                            <td><input type="text" name="prereq_course_number" /></td>
                            <td><input type="text" name="prereq_number" /></td>
                        </tr>
                    <?php } ?>
                    <tr>
                        <td colspan="5" style="text-align: center"><input type="submit" value="Add" /></td>
                    </tr>
                </table>
            </form>
        <?php } else { ?>
            <h3> <a href="index1.php?mn=0">Home</a> Transcript </h3>
            <table>
                <style> table, th, td{
                        border: 1px solid black;
                        border-collapse: collapse;
                    }
                    th, td {
                        padding : 5px;
                    }
                    th {
                        background-color: grey;
                    }
                </style>

                <tr> <th rowspan="2">Student_Name</th>  <th colspan="5">Student_transcript</th> </tr>
                <tr>
                    <th style="width: 8em">Course_number</th>
                    <th style="width: 8em">Grade</th>
                    <th style="width: 8em">Semester</th>
                    <th style="width: 8em">Year</th>
                    <th style="width: 8em">Section_id</th>
                </tr>
                <?php
                for ($j = 0; $j < count($trans_student_name); $j++) {
                    ?>
                    <tr>
                        <td rowspan="<?php print sizeof($trans_student_INFO[$j]); ?>" style="text-align: center"><?php print $trans_student_name[$j]; ?></td>
                        <?php
                        //if statement used to Fix error for printing first variable in 2D Array
                        if ($j <> 0) {
                            ?>
                            <td style="text-align: center"><?php print $trans_student_INFO[$j][0]; ?></td>
                            <td style="text-align: center"><?php print $trans_student_grade[$j][0]; ?></td>
                            <td style="text-align: center"><?php print $trans_student_semester[$j][0]; ?></td>
                            <td style="text-align: center"><?php print $trans_student_year[$j][0]; ?></td>
                            <td style="text-align: center"><?php print $trans_student_sectionID[$j][0]; ?></td>
                        </tr>
                        <?php
                    }
                    for ($i = 1; $i < count($trans_student_INFO[$j]); $i++) {
                        ?>
                        <tr><td style="text-align: center"><?php print $trans_student_INFO[$j][$i]; ?></td>
                            <td style="text-align: center"><?php print $trans_student_grade[$j][$i]; ?></td>
                            <td style="text-align: center"><?php print $trans_student_semester[$j][$i]; ?></td>
                            <td style="text-align: center"><?php print $trans_student_year[$j][$i]; ?></td>
                            <td style="text-align: center"><?php print $trans_student_sectionID[$j][$i]; ?></td></tr>

                        <?php
                    }
                }
                ?>
            </tr>

        </table>
<?php } ?>
</body>
</html>
<?php
mysql_close($con);
?>