    .text
    .globl main

main:
    
    addi $sp, $sp, -4
    sw   $ra, 0($sp) 
    jal  func1       
    lw   $ra, 0($sp)  
    addi $sp, $sp, 4 
    jr   $ra          

func1:
    addi $sp, $sp, -4
    sw   $ra, 0($sp)
    # jump to func2
    jal  func2
    lw   $ra, 0($sp)
    addi $sp, $sp, 4
    jr   $ra
func2:
    addi $sp, $sp, -4
    sw   $ra, 0($sp)
    
    li   $t2, 5             
    li   $t3, 10            

    bne  $t2, $t3, func3    
    j    end  
    
    
 func3:
    addi $sp, $sp, -4
    sw   $ra, 0($sp)
   
    li   $t4, 15          
    li   $t5, 20           

    slt  $t6, $t4, $t5      
    beq  $t6, $zero, end    
    jal  func4              

    lw   $ra, 0($sp)
    addi $sp, $sp, 4
    jr   $ra
func4:
    addi $sp, $sp, -4
    sw   $ra, 0($sp)
   
    li   $t7, 30          
    li   $t8, 25            

    slti $t9, $t8, 30       
    beq  $t9, $zero, end   
    jal  func5              

    lw   $ra, 0($sp)
    addi $sp, $sp, 4
    jr   $ra
func5:
    addi $sp, $sp, -4
    sw   $ra, 0($sp)

  
    j    end      


 
    
end:
halt
    
    
    
