<?php
/*
Author: Xinghe Lu
Date: 3/11/2016
Purpose: This file provides three function 1)get all movie details from database; 2)get movie details upon the specific movieID
offered by the user; 3) get movies which have ratings higer than a specific float number defined by the user
Version 1.0
*/

function sayHi(){
	echo "<h1>Hello World!</h1>";
}

function getDB(){
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

function getMovies (){
	
	$conn = getDB();
	$sql = "SELECT * FROM movies ORDER BY name ASC";
	
	if(!$result = $conn->query($sql)){
		die($conn->error);
	}
	
	$return_arr = array();
	
	while($row = $result->fetch_assoc()){
		$row_array['id'] = $row['id'];
		$row_array['name'] = $row['name'];
		$row_array['description'] = $row['description'];
		$row_array['stars'] = $row['stars'];
		$row_array['length'] = $row['length'];
		$row_array['image'] = $row['image'];
		$row_array['year'] = "".$row['year'];
		$row_array['rating'] = $row['rating'];
		$row_array['director'] = $row['director'];
		$row_array['url'] = $row['url'];
		
		array_push($return_arr,$row_array);
	}
	
	echo json_encode($return_arr);
	
	$conn->close();
}

function getMoviesAboveRating($rating){
	$conn = getDB();
	
	if($stmt = $conn->prepare("SELECT id, name, description, stars, length, image, year, rating, director, url
								FROM Movies
								WHERE rating >= ?
								ORDER BY rating DESC")){
									
		$stmt->bind_param("s", $rating);
		
		$stmt->execute();
		
		$res = $stmt->get_result();
		
		/*$return_arr = array();
		while($row = $res->fetch_assoc()){
			array_push($return_arr, $row);
		}*/
		$return_arr = array();
	
		while($row = $res->fetch_assoc()){
			$row_array['id'] = $row['id'];
			$row_array['name'] = $row['name'];
			$row_array['description'] = $row['description'];
			$row_array['stars'] = $row['stars'];
			$row_array['length'] = $row['length'];
			$row_array['image'] = $row['image'];
			$row_array['year'] = "".$row['year'];
			$row_array['rating'] = "".$row['rating'];
			$row_array['director'] = $row['director'];
			$row_array['url'] = $row['url'];
			
			array_push($return_arr,$row_array);
		}
		
		
		$stmt->close();
	}
	
	echo json_encode($return_arr);
	$conn->close();
}

function getMoviesDetail($id){
	$conn = getDB();
	
	if($stmt = $conn->prepare("SELECT id, name, description, stars, length, image, year, rating, director, url
								FROM Movies
								WHERE id = ?
								ORDER BY rating DESC")){
									
		$stmt->bind_param("s", $id);
		
		$stmt->execute();
		
		$res = $stmt->get_result();
		
		
		
		$return_arr = array();
	
		while($row = $res->fetch_assoc()){
			$row_array['id'] = $row['id'];
			$row_array['name'] = $row['name'];
			$row_array['description'] = $row['description'];
			$row_array['stars'] = $row['stars'];
			$row_array['length'] = $row['length'];
			$row_array['image'] = $row['image'];
			$row_array['year'] = "".$row['year'];
			$row_array['rating'] = "".$row['rating'];
			$row_array['director'] = $row['director'];
			$row_array['url'] = $row['url'];
			
			array_push($return_arr,$row_array);
		}
		
		$stmt->close();
	}
	
	echo json_encode($return_arr);
	$conn->close();
}
?>