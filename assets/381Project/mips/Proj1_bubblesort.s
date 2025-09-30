.data
    array: .word 64, 34, 25, 12, 22, 11, 90   
    size: .word 7                             

.text
.globl main

main:

    li $s0, 0                
    lw $s1, size              
    addi $s1, $s1, -1         
    
outer_loop:
    beq $s0, $s1, end          
    

    li $s2, 0                 
    sub $s3, $s1, $s0         
    
inner_loop:
    beq $s2, $s3, inner_done  
    
    la $t0, array              
    sll $t1, $s2, 2            
    add $t1, $t1, $t0         
    lw $t2, 0($t1)             
    lw $t3, 4($t1)            
    

    ble $t2, $t3, no_swap    

    sw $t3, 0($t1)            
    sw $t2, 4($t1)           
    
no_swap:
    addi $s2, $s2, 1          
    j inner_loop
    
inner_done:
    addi $s0, $s0, 1          
    j outer_loop
    
end:
halt

