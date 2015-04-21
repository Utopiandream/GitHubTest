<?php

$id = intval($_GET['id']);
$val = $_GET['val'];

$dbhost = "localhost";
$dbuser = "root";
$dbpassword = "";
$dbname = "university";

$con = mysql_connect($dbhost, $dbuser, $dbpassword);

if (!$con) {
    die('Could not connect: ' . mysql_error());
}

mysql_select_db($dbname, $con);

$query = "UPDATE student SET student_number = '$val' WHERE id = $id";
mysql_query($query);

mysql_close($con);

print $val;
?>