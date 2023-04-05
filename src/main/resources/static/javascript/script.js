console.log("java script working")
const togglesidebar=()=>{
	
	if($('.sidebar').is(":visible")){
		
		//true means we have to hide the sidebar
		
		$(".sidebar").css("display","none");
		$(".content").css("margin-left","0%");
	}else{
		//false means we have to show side bar
		$(".sidebar").css("display","block");
		$(".content").css("margin-left","30%");
		
	}
	
	
};


const search=()=>{
	
	console.log("Searching.....");
	
	let query=$("#search-input").val();
	
	if(query==''){
		$(".search-result").hide();
	}else{
		console.log(query);
		
		let url=`http://localhost:8889/search/${query}`;
		
		fetch(url).then(response=>{
			return response.json();
		}).then(data=>{
			console.log(data);
			
			let text=`<div class='list-group' >`;
			
			data.forEach((contact) =>{
				
				text +=`<a href='/user/${contact.cid}/contact' class='list-group-item list-group-item-action'> ${contact.cname}</a>`
			})
			
			
			text+=`</div>`;
			
			$(".search-result").html(text);
			$(".search-result").show();
		})
		
		
		
	}
	
	
}