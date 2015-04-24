<?php
if (isset($_GET['mn'])) {
    $mn = intval($_GET['mn']);
} else {
    $mn = 0;
}
$id = ($_GET['id']);
$val = $_GET['val'];
$rn = $_GET['rn'];

$dbhost = "localhost";
$dbuser = "root";
$dbpassword = "";
$dbname = "university";

$con = mysql_connect($dbhost, $dbuser, $dbpassword);


$inputArr = split("~", $val);

if (!$con) {
    die('Could not connect: ' . mysql_error());
}

mysql_select_db($dbname, $con);
if($mn == 0){

 $query = "UPDATE student SET student_number = '$inputArr[0]', name = '$inputArr[1]',"
         . " myclass = '$inputArr[2]', major = '$inputArr[3]' WHERE student_number = $id";
 mysql_query($query);

}


if($mn == 1){

 $query = "UPDATE course SET course_number = '$inputArr[0]', course_name = '$inputArr[1]',"
         . " credit_hours = '$inputArr[2]', department = '$inputArr[3]' WHERE course_number = $id";
 mysql_query($query);

}

    
    $out = "$val";
   


mysql_close($con);

print $out;
?>