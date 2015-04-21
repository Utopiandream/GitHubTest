<?php

if (isset($_GET['mn'])) {
    $mn = intval($_GET['mn']);
} else {
    $mn = 0;
}

$id = ($_GET['id']);

$dbhost = "localhost";
$dbuser = "root";
$dbpassword = "";
$dbname = "university";

$con = mysql_connect($dbhost, $dbuser, $dbpassword);

if (!$con) {
    die('Could not connect: ' . mysql_error());
}

mysql_select_db($dbname, $con);

$query = "SELECT * FROM  student WHERE student_number = $id";
$result = mysql_query($query);

if ($result) {
    $row = mysql_fetch_assoc($result);
    $out = $row['student_number'];
}


mysql_close($con);

print $out;
?>