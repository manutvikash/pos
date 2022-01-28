function getBrandUrl(){
	var baseUrl=$("meta[name=baseUrl]").attr("content");
	console.log(baseUrl);
	return baseUrl+"/api/brand";
}