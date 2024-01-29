BEGIN {
    x = 1;
    y = 0;
    
    xAsBool = x ? "true" : "false";
    yAsBool = y ? "true" : "false";
    
    first = (x && y) ? "success" : "fail";
    second = (x || y) ? "success" : "fail";
    
    k = 12 * 2
    k -= 3
    j = (3^(1+1))/2
    
    w = k + j
    
    if(w < 3){
        n = "this cant be true..."
    }else if(w == 13){
        n = "this cant be true..."
    }else if(w > 1){
        n = "wow this is true!!!"
    }else{
        n = "last resort: wont be true"
    }
}