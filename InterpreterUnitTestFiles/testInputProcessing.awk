BEGIN {FS=","; sum = 0}

{
    printf "STUDENT: %s %s - GPA: %s\n",$1,$2,$3
    sum += $3
}

END{
    print "\nAVERAGE GPA:", (avgGPA = sum/FNR)
}