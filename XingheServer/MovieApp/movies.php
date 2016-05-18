<?php
/*
Author: Xinghe Lu
Date: 3/11/2016
Purpose: This file provides two functions 1)delete movie data from the database; 2)add movie data to the database
Version 1.0
*/

function sayHiMovie(){//test
	echo "<h1>Hello World 2!</h1>";
}

function getDatabase(){//connect to database
	$dbhost="localhost";
	$dbuser="root";
	$dbpass="";
	$dbname="androidMovieDB";
	
	$conn = new mysqli($dbhost, $dbuser, $dbpass, $dbname);
	if($conn->connect_error){
		die($conn->connect_error);
		
	}
	
	return $conn;

}

function deleteMovie($id){//delete a movie

	$conn = getDatabase();
	
	if ($conn->connect_error) {
		die("Connection failed: " . $conn->connect_error);
	} 

	// delete a record
	$sql = "DELETE FROM Movies WHERE id='$id'";

	if ($conn->query($sql) === TRUE) {
		echo "Record deleted successfully";
	} else {
		echo "Error deleting record: " . $conn->error;
	}

	$conn->close();

}

function addMovie($id, $name, $description, $stars, $length, $image, $year, $rating, $director, $url){//add a movie

	$conn = getDatabase();
	
	if ($conn->connect_error) {
		die("Connection failed: " . $conn->connect_error);
	} 

	// insert a new row to record
	$sql = "INSERT INTO Movies (id, name, description, stars, length, image, year, rating, director, url)
				VALUES ('$id', '$name', '$description', '$stars', '$length', '$image', '$year', '$rating', '$director', '$url')";

	if ($conn->query($sql) === TRUE) {
		echo "New movie data created successfully";
	} else {
		echo "Error insering data: " . $conn->error;
	}

	$conn->close();

}

?>