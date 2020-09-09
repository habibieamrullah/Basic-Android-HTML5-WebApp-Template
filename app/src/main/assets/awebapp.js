function showBanner(){
	try{
		Android.showAndroidBanner()
	}catch(e){
		console.log(e)
	}
}

function hideBanner(){
	try{
		Android.hideAndroidBanner()
	}catch(e){
		console.log(e)
	}
}

function landscape(){
	try{
		Android.landscape()
	}catch(e){
		console.log(e)
	}
}

function portrait(){
	try{
		Android.portrait()
	}catch(e){
		console.log(e)
	}
}

function showInterstitial(){
	try{
		Android.showAd()
	}catch(e){
		console.log(e)
	}
}