<?php
$id = $_GET['id'];
$name = $_GET['name'];
$link = mysqli_connect('localhost', 'root', '') or die('Cannot connect to the DB');
mysqli_select_db($link, 'db_lokasi') or die('Cannot select the DB');
/* grab the posts from the db */
$query = "delete from testcrud where id = '".$id."'" ;
$result = mysqli_query($link,$query) or die('Error query: '.$query);
echo "SUCCESS";
?>