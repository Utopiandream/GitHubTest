<?php

if (isset($_GET['mn'])) {
    $mn = intval($_GET['mn']);
} else {
    $mn = 0;
}
$id = ($_GET['id']);
$val = $_GET['val'];
$rn = $_GET['rn'];

$inputArr = split("~", $val);

$dbhost = "localhost";
$dbuser = "root";
$dbpassword = "";
$dbname = "university";

$con = mysql_connect($dbhost, $dbuser, $dbpassword);


if (!$con) {
    die('Could not connect: ' . mysql_error());
}

mysql_select_db($dbname, $con);

    $out = "$val";
   

mysql_close($con);

print $out;
?>