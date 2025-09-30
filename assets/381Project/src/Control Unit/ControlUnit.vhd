LIBRARY IEEE;
USE ieee.std_logic_1164.ALL;
USE ieee.numeric_std.ALL;

ENTITY ControlUnitTest IS
  PORT (
    OpCode : IN STD_LOGIC_VECTOR(5 DOWNTO 0);
    Funct : IN STD_LOGIC_VECTOR(5 DOWNTO 0);
    ALUControlBits : OUT STD_LOGIC_VECTOR(3 DOWNTO 0);
    jump : OUT STD_LOGIC;
    regDst : OUT STD_LOGIC;
    ALUsrc : OUT STD_LOGIC;
    memToReg : OUT STD_LOGIC;
    regWrite : OUT STD_LOGIC;
    memRead : OUT STD_LOGIC;
    memWrite : OUT STD_LOGIC;
    branch : OUT STD_LOGIC;
    brlShftDir : OUT STD_LOGIC;
    lui : OUT STD_LOGIC;
    bSigned : OUT STD_LOGIC;
    aluOver : out STD_LOGIC_VECTOR(3 downto 0);
    jr : out STD_LOGIC;
    bne : out STD_LOGIC;
    extsigned : out STD_LOGIC
  );
END ControlUnitTest;

ARCHITECTURE sim OF ControlUnitTest IS
  SIGNAL ALUControlOveride : STD_LOGIC_VECTOR(3 DOWNTO 0);
  SIGNAL ALUOp : STD_LOGIC_VECTOR(1 DOWNTO 0);
BEGIN
  
 
process(OpCode, Funct, regDst, ALUsrc, memToReg, regWrite, memRead, memWrite, branch, brlShftDir, lui, bSigned, jump, ALUOp, ALUControlOveride, ALUControlBits)
  begin
    -- Default signal assignments
    regDst <= '0';
    ALUsrc <= '0';
    memToReg <= '0';
    regWrite <= '0';
    memRead <= '0';
    memWrite <= '0';
    branch <= '0';
    brlShftDir <= '0';
    lui <= '0';
    bSigned <= '1';
    jump <= '0';
    jr <= '0';
    bne <= '0';
    extsigned <= '1';

    CASE OpCode IS
      -- R-type instruction
      WHEN "000000" => 
        ALUOp <= "01";
        regDst <= '1';
        ALUsrc <= '0';
        regWrite <= '1';
    
  --halt
      WHEN "010100" =>
      ALUControlOveride <= "XXXX";
      ALUOp <= "11";
      ALUsrc <= '1';
      regWrite <= '0';

  -- lw
      WHEN "100011" => 
        ALUOp <= "00";
        ALUControlOveride <= "0010";
        ALUsrc <= '1'; 
        memToReg <= '1'; 
        regWrite <= '1'; 
        memRead <= '1';
        bSigned <= '0';
        regDst <= '0';
 -- bne
      WHEN "000101" => 
        ALUOp <= "11";
        ALUControlOveride <= "0110";
        branch <= '1'; 
        regWrite <= '0';
	bne <= '1';
        -- addiu
      WHEN "001001"  =>  
        ALUOp <= "11";
        ALUControlOveride <= "0010";
       
        regWrite <= '1';
        ALUsrc <= '1';
        bSigned <= '0';
      -- xori
      WHEN "001110"  =>  
      ALUOp <= "11";
      ALUControlOveride <= "1111";
      extsigned <= '0';
      regWrite <= '1';
      ALUsrc <= '1';
  --addi      
        WHEN "001000" =>
        ALUOp <= "11";
        ALUControlOveride <= "0010";
        regDst <= '0';
        ALUsrc <= '1';
        memToReg <= '0';
        regWrite <= '1';
        memRead <= '0';
        memWrite <= '0';
  -- lui
      WHEN "001111"  => 
        ALUOp <= "11";
        ALUControlOveride <= "0100";
        regWrite <= '1';
        --lui <= '1';
 -- andi
      WHEN "001100"=>
        ALUOp <= "11";
        ALUControlOveride <= "0000";
        extsigned <= '0';
        ALUsrc <= '1'; 
        regWrite <= '1';
 -- ori
      WHEN "001101" =>
        ALUOp <= "11";
        ALUControlOveride <= "0001";
        extsigned <= '0';
        ALUsrc <= '1'; 
        regWrite <= '1';
-- slti
      WHEN "001010" =>

        ALUOp <= "11";
        ALUControlOveride <= "0111";
        ALUsrc <= '1'; 
        regWrite <= '1';
 -- beq
      WHEN "000100" => 
        ALUOp <= "11";
        ALUControlOveride <= "0110";
        branch <= '1'; 
        regWrite <= '0';
-- sw
      WHEN "101011" =>
        ALUOp <= "11";
        ALUControlOveride <= "0010";
        ALUsrc <= '1';
        memWrite <= '1';
        regWrite <= '0';
        bSigned <= '1';

        --jump
      WHEN "000010" =>
        ALUOp <= "11";
        ALUControlOveride <= "XXXX";--might need to be changed
        jump <= '1';
        regDst<='1';
-- jal
      WHEN "000011" =>
        ALUOp <= "11";
        ALUControlOveride <= "1011";--might need to be changed
        jump <= '1';
        regWrite <='1';


      WHEN OTHERS =>
        ALUOp <= "11";
        ALUControlOveride <= "1010"; 
    END CASE;

   
    CASE ALUOp IS
      WHEN "00" => ALUControlBits <= "0010";  -- Load/store
      WHEN "01" =>  -- R-type instructions
        CASE Funct IS
          WHEN "100000" => 
          ALUControlBits <= "0010";  -- ADD
          WHEN "100001" => 
            ALUControlBits <= "0010";
            bSigned <= '0';  -- ADDU
          WHEN "100010" => ALUControlBits <= "0110";  -- SUB
          WHEN "100011" => 
            ALUControlBits <= "0110";  -- SUBU
            bSigned <= '0';
          WHEN "100100" => ALUControlBits <= "0000";  -- AND
          WHEN "100101" => ALUControlBits <= "0001";  -- OR
          WHEN "100111" => ALUControlBits <= "1100";  -- NOR
          WHEN "101010" => ALUControlBits <= "0111";  -- SLT
          WHEN "000000" => ALUControlBits <= "1000";  -- SLL
          WHEN "000010" => ALUControlBits <= "1101";  -- SRL
          WHEN "000011" => ALUControlBits <= "1001";  -- SRA
          WHEN "100110" => ALUControlBits <= "1111";  -- XOR
          WHEN "001000" => 
            ALUControlBits <= "XXXX";  -- jr
            jump<= '1';
            regWrite <= '0';
            jr <= '1'; 

          WHEN OTHERS => ALUControlBits <= "XXXX";  
        END CASE;
      WHEN "10" => ALUControlBits <= "0110";  -- BEQ
      WHEN OTHERS => ALUControlBits <= ALUControlOveride;  
    END CASE;

    aluOver <= ALUControlOveride;
    end process;

  


END sim;



