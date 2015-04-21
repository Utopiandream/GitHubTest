<?php

$id = intval($_GET['id']);

$dbhost = "localhost";
$dbuser = "root";
$dbpassword = "";
$dbname = "university";

$con = mysql_connect($dbhost, $dbuser, $dbpassword);

if (!$con) {
    die('Could not connect: ' . mysql_error());
}

mysql_select_db($dbname, $con);

$query = "SELECT name FROM  student WHERE student_number = $id";
$result = mysql_query($query);

if ($result) {
    $row = mysql_fetch_assoc($result);
    $out = $row['name'];
}

mysql_close($con);

print $out;
?>