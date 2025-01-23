console.log("Smart contact programmer")

const toggleSidebar = () => {


	if ($('.sidebar').is(":visible")) {

		$(".sidebar").css("display", "none")
		$(".content").css("margin-left", "0%")

	} else {

		$(".sidebar").css("display", "block")
		$(".content").css("margin-left", "20%")

	}
};


const search = () => {

	//console.log("Search");

	let query = $("#search-input").val();


	if (query == "") {
		$(".search-result").hide();

	} else {
		// search 
		console.log(query);


		// sending request to server
		let url = `http://localhost:8080/search/${query}`;
		fetch(url).then((responce) => {
			return responce.json();
		}).then((data) => {
			// data 
			console.log(data);
		})

		$(".search-result").show();
	}
};

