<?php
$username = 'fajar';
$password = 'p@ssw0rd';
$database = 'db_lokasi';


function parseToXML($htmlStr)
{
$xmlStr=str_replace('<','&lt;',$htmlStr);
$xmlStr=str_replace('>','&gt;',$xmlStr);
$xmlStr=str_replace('"','&quot;',$xmlStr);
$xmlStr=str_replace("'",'&#39;',$xmlStr);
$xmlStr=str_replace("&",'&amp;',$xmlStr);
return $xmlStr;
}

// Opens a connection to a MySQL server
$connection=mysqli_connect ('166.62.28.127', $username, $password);
if (!$connection) {
  die('Not connected : ' . mysqli_error());
}

// Set the active MySQL database
$db_selected = mysqli_select_db("db_lokasi", $connection);
if (!$db_selected) {
  mysqli_error();
}

// Select all the rows in the markers table
$query = "SELECT * FROM lokasi";
$result = mysqli_query($query);
if (!$result) {
  mysqli_error();
}

header("Content-type: text/xml");

// Start XML file, echo parent node
echo "<?xml version='1.0' ?>";
echo '<markers>';
$ind=0;
// Iterate through the rows, printing XML nodes for each
while ($row = @mysqli_fetch_assoc($result)){
  // Add to XML document node
  echo '<marker ';
  echo 'id="' . $row['userid'] . '" ';
  echo 'name="' . parseToXML($row['jamaah']) . '" ';
  echo 'Jenis Kelamin="' . parseToXML($row['jeniskelamin']) . '" ';
  echo 'lat="' . $row['latitude'] . '" ';
  echo 'lng="' . $row['longitude'] . '" ';
  echo 'type="' . $row['jeniskelamin'] . '" ';
  echo '/>';
  $ind = $ind + 1;
}

// End XML file
echo '</markers>';

?>