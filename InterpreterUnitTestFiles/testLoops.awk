BEGIN {
    x = 7

    while (1) {
        if (x > 15) {
            print "Breaking loop: x is greater than 15"
	    break
	}
        x += 3
    }
	
    y = x
    
    for(k = 0; k < 10; k++){
       y--
    }
    fin = fn()
}

function fn(){
   string = "fn returned after loop: "
   j = 1;
   while(1){
	if(j = x + y){
	   return string j
	}
	j++
   }
}