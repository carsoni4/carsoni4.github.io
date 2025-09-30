-------------------------------------------------------------------------
-- Henry Duwe
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- MIPS_Processor.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains a skeleton of a MIPS_Processor  
-- implementation.

-- 01/29/2019 by H3::Design created.
-------------------------------------------------------------------------


library IEEE;
use IEEE.std_logic_1164.all;
USE ieee.numeric_std.ALL;
library work;
use work.MIPS_types.all;

entity MIPS_Processor is
  generic(N : integer := DATA_WIDTH);
  port(iCLK            : in std_logic;
       iRST            : in std_logic;
       iInstLd         : in std_logic;
       iInstAddr       : in std_logic_vector(N-1 downto 0);
       iInstExt        : in std_logic_vector(N-1 downto 0);
       oALUOut         : out std_logic_vector(N-1 downto 0)); -- TODO: Hook this up to the output of the ALU. It is important for synthesis that you have this output that can effectively be impacted by all other components so they are not optimized away.

end MIPS_Processor;


architecture structure of MIPS_Processor is

  -- Required data memory signals
  signal s_DMemWr       : std_logic; -- TODO: use this signal as the final active high data memory write enable signal
  signal s_DMemAddr     : std_logic_vector(N-1 downto 0); -- TODO: use this signal as the final data memory address input
  signal s_DMemData     : std_logic_vector(N-1 downto 0); -- TODO: use this signal as the final data memory data input
  signal s_DMemOut      : std_logic_vector(N-1 downto 0); -- TODO: use this signal as the data memory output
 
  -- Required register file signals 
  signal s_RegWr        : std_logic; -- TODO: use this signal as the final active high write enable input to the register file
  signal s_RegWrAddr    : std_logic_vector(4 downto 0); -- TODO: use this signal as the final destination register address input
  signal s_RegWrData    : std_logic_vector(N-1 downto 0); -- TODO: use this signal as the final data memory data input

  -- Required instruction memory signals
  signal s_IMemAddr     : std_logic_vector(N-1 downto 0); -- Do not assign this signal, assign to s_NextInstAddr instead
  signal s_NextInstAddr : std_logic_vector(N-1 downto 0); -- TODO: use this signal as your intended final instruction memory address input.
  signal s_Inst         : std_logic_vector(N-1 downto 0); -- TODO: use this signal as the instruction signal 

  -- Required halt signal -- for simulation
  signal s_Halt         : std_logic;  -- TODO: this signal indicates to the simulation that intended program execution has completed. (Opcode: 01 0100)

  -- Required overflow signal -- for overflow exception detection
  signal s_Ovfl         : std_logic;  -- TODO: this signal indicates an overflow exception would have been initiated

  component mem is
    generic(ADDR_WIDTH : integer;
            DATA_WIDTH : integer);
    port(
          clk          : in std_logic;
          addr         : in std_logic_vector((ADDR_WIDTH-1) downto 0);
          data         : in std_logic_vector((DATA_WIDTH-1) downto 0);
          we           : in std_logic := '1';
          q            : out std_logic_vector((DATA_WIDTH -1) downto 0));
    end component;

  -- TODO: You may add any additional signals or components your implementation 
  --       requires below this comment

    -- Control unit signals
  signal s_ALUControlBits : std_logic_vector(3 downto 0);
  signal s_regDst : std_logic;
  signal s_ALUsrc : std_logic;
  signal s_memToReg : std_logic;
  signal s_memRead : std_logic;
  signal s_memWrite : std_logic;
  signal s_branch : std_logic;
  signal s_brlShftDir : std_logic;
  signal s_lui : std_logic;
  signal s_bSigned : std_logic;
  signal s_jump : std_logic;
  signal s_jr : std_logic;
  signal s_bne : std_logic;
  signal s_extsigned : std_logic;
  signal s_WB : STD_LOGIC;

  --ALU signals
  signal s_ALUin_A : STD_LOGIC_VECTOR(31 downto 0);
  signal s_ALUin_B : STD_LOGIC_VECTOR(31 downto 0);
  signal s_Zero : std_logic;
  signal s_aluSrcMuxO : std_logic_vector(31 downto 0); 
  signal s_immExt : std_logic_vector(31 downto 0);
  signal s_ReadData1 : std_logic_vector(31 downto 0);
  signal s_ReadData2 : std_logic_vector(31 downto 0);
  signal s_jalMux : std_logic;

  --Branch signals
  signal s_BranchAndZero : std_logic;
  signal s_regIsEqual : std_logic;
  signal s_wrMuxCorrected : std_logic_vector(4 downto 0);

  --Mux
  signal s_wrMuxO : std_logic_vector(4 downto 0); 
  signal aluResultMuxO : std_logic_vector(31 downto 0);

  --Constant used for Jal [IDK if still used]
  signal s_Constant: std_logic_vector(31 downto 0) := x"00000004";

  --IFID
  signal s_IFID_adder_out : std_logic_vector(31 downto 0);
  signal s_IFID_imem_out : std_logic_vector(31 downto 0);

  --IDEX
  signal s_IDEX_reg1_out : std_logic_vector(31 downto 0);
  signal s_IDEX_reg2_out : std_logic_vector(31 downto 0);
  signal s_IDEX_signextension_out : std_logic_vector(31 downto 0);
  signal s_IDEX_alucontrol_out : STD_LOGIC_VECTOR(31 downto 0);
  signal s_IDEX_currentInstruction_out : STD_LOGIC_VECTOR(31 downto 0);
  signal s_IDEX_nextAddr_out : STD_LOGIC_VECTOR(31 downto 0);

  --IDEX Control Signal
  signal s_IDEXCS_jump_out : STD_LOGIC; 
  signal s_IDEXCS_regDst_out : STD_LOGIC; 
  signal s_IDEXCS_ALUsrc_out : STD_LOGIC; 
  signal s_IDEXCS_memToReg_out : STD_LOGIC; 
  signal s_IDEXCS_regWrite_out : STD_LOGIC; 
  signal s_IDEXCS_memRead_out : STD_LOGIC; 
  signal s_IDEXCS_memWrite_out : STD_LOGIC; 
  signal s_IDEXCS_branch_out : STD_LOGIC;  
  signal s_IDEXCS_bSigned_out : STD_LOGIC; 
  signal s_IDEXCS_jr_out : STD_LOGIC; 
  signal s_IDEXCS_bne_out : STD_LOGIC; 
  signal s_IDEXCS_extsigned_out : STD_LOGIC;
  
  --MEMWB
  signal s_MEMWB_dmem_out   : std_logic_vector(31 downto 0);
  signal s_MEMWB_alu_out    : std_logic_vector(31 downto 0);
  signal s_MEMWB_wrMuxO_out : STD_LOGIC_VECTOR(31 downto 0);
  signal s_MEMWB_nextAddr_out : STD_LOGIC_VECTOR(31 downto 0);
  signal s_MEMWB_halt_out : STD_LOGIC_VECTOR(31 downto 0);
  signal RUBBERDUCK : STD_LOGIC_VECTOR(31 downto 0);
 

  --EXMEM Control Signal
  signal s_EXMEMCS_jump_out : STD_LOGIC;
  signal s_EXMEMCS_jalMux_out : STD_LOGIC;
  signal s_EXMEMCS_memToReg_out : STD_LOGIC;
  signal s_EXMEMCS_regWrite_out : STD_LOGIC;
  signal s_EXMEMCS_memRead_out : STD_LOGIC;
  signal s_EXMEMCS_memWrite_out : STD_LOGIC;
  signal s_EXMEMCS_branch_out : STD_LOGIC;
  signal s_EXMEMCS_bSigned_out : STD_LOGIC;
  signal s_EXMEMCS_jr_out : STD_LOGIC;
  signal s_EXMEMCS_bne_out : STD_LOGIC;
  signal s_EXMEMCS_extsigned_out : STD_LOGIC;

  --EXMEM
  signal s_EXMEM_alu_out : STD_LOGIC_VECTOR(31 downto 0);
  signal s_EXMEM_aluSrc_out : STD_LOGIC_VECTOR(31 downto 0);
  signal s_EXMEM_wrMuxO_out : STD_LOGIC_VECTOR(31 downto 0);
  signal s_EXMEM_nextAddr_out : STD_LOGIC_VECTOR(31 downto 0);
  signal s_EXMEM_reg2_out : STD_LOGIC_VECTOR(31 downto 0);
  signal s_EXMEM_currentInstruction_out : STD_LOGIC_VECTOR(31 downto 0);

  --MEMWB Control Signal
  signal s_MEMWBCS_jalMux_out : STD_LOGIC;
  signal s_MEMWBCS_memToReg_out : STD_LOGIC;
  signal s_MEMWBCS_memRead_out : STD_LOGIC;
  signal s_MEMWBCS_memWrite_out : STD_LOGIC;  

  --Register Signal
  signal s_Read1Bypass : STD_LOGIC_VECTOR(31 downto 0);
  signal s_Read2Bypass : STD_LOGIC_VECTOR(31 downto 0);

  --Signal for halt (MIGHT NEED AFTER EACH STAGE BUT IDK :))
  signal imemHalt : STD_LOGIC;

  --TODO CONSIDER FETCH LOGIC MIGHT NEED TO BE USED IF BRANCH IS BEFORE HALT OR SOME SHIT IDK (WHAT HAPPENS IF BRANCH OR JUMP BEFORE HALT [BRAIN EXPLODE])

  --Forwarding Unit
  signal s_FU_rs : STD_LOGIC_VECTOR(1 downto 0);
  signal s_FU_rt : STD_LOGIC_VECTOR(1 downto 0);
  signal s_FU_SW : STD_LOGIC_VECTOR(1 downto 0);
  signal s_SW_takeover : STD_LOGIC_VECTOR(31 downto 0);
  --TODO Mux Select for Branch and JR Logic 
  signal s_FU_IDRS : STD_LOGIC_VECTOR(1 downto 0);
  signal s_FU_IDRT : STD_LOGIC_VECTOR(1 downto 0);
  --Output of Mux for Branch and JR
  signal s_FU_IDRS_out : STD_LOGIC_VECTOR(31 downto 0);
  signal s_FU_IDRT_out : STD_LOGIC_VECTOR(31 downto 0);
  signal s_FU_branchCorrect : STD_LOGIC_VECTOR(4 downto 0);

  --Hazard Detection
  signal s_HD_flush : STD_LOGIC;
  signal s_HD_stall : STD_LOGIC; 
  signal s_HD_aluCode : STD_LOGIC_VECTOR(3 downto 0);
  signal s_HD_RW : STD_LOGIC;
  signal s_HD_MW : STD_LOGIC;
  signal s_HD_MR : STD_LOGIC;
  signal s_HD_MTR : STD_LOGIC;
  signal s_HD_RD : STD_LOGIC;

  --register file
  component mipsreg
    Port(
    clk : in STD_LOGIC;
    i_RST : in STD_LOGIC;
    reg_write : in STD_LOGIC;
    read_reg1 : in STD_LOGIC_VECTOR(4 downto 0);
    read_reg2 : in STD_LOGIC_VECTOR(4 downto 0);
    write_reg : in STD_LOGIC_VECTOR(4 downto 0);
    write_data : in STD_LOGIC_VECTOR(31 downto 0);
    read_data1 : out STD_LOGIC_VECTOR(31 downto 0);
    read_data2 : out STD_LOGIC_VECTOR(31 downto 0);
    halt       : in std_logic
    );
    end component;

    --fetch logic
    component fetch is 
    port(
        iCLK : in std_logic;
        iJumpAddr : in std_logic_vector(31 downto 0);
        zbAnd : in std_logic;
        iRST : in std_logic;
        iJ : in std_logic;
        iLOAD : in std_logic;
        iExtender : in std_logic_vector(31 downto 0);
        oA : out std_logic_vector(31 downto 0);
        jr : in std_logic;
        jrRead : in std_logic_vector(31 downto 0);
        halt : in std_logic
    );
    end component;

    --ALU
    component ALU is
      port (
          A         : in  std_logic_vector(31 downto 0);  -- first register
          B         : in  std_logic_vector(31 downto 0);  -- second register       
          Control   : in  std_logic_vector(3 downto 0);   -- 4-bit control signal
          shamt     : in std_logic_vector(4 downto 0); --shamt
          lui_in    : in std_logic_vector(15 downto 0); --lui immediate
          repl_in   : in std_logic_vector(7 downto 0); --repl
          addsubsigned : in std_logic; --signed when 1 unsigned when 0
          Result    : out std_logic_vector(31 downto 0);  -- Output of ALU
          Zero      : out std_logic;                      -- Flag to indicate if Result is zero
          CarryO    : out std_logic;                      --flag for carry out
          Overflow  : out std_logic;
          jalmux_o  : out std_logic;
	        bne       : in std_logic
      );
    end component;


    -- Control Unit Component
  component ControlUnitTest is
    port (
      OpCode          : IN std_logic_vector(5 DOWNTO 0);
      Funct           : IN std_logic_vector(5 DOWNTO 0);
      ALUControlBits  : OUT std_logic_vector(3 DOWNTO 0);
      regDst          : OUT std_logic;
      ALUsrc          : OUT std_logic;
      memToReg        : OUT std_logic;
      regWrite        : OUT std_logic;
      memRead         : OUT std_logic;
      memWrite        : OUT std_logic;
      branch          : OUT std_logic;
      brlShftDir      : OUT std_logic;
      lui             : OUT std_logic;
      bSigned         : OUT std_logic;
      jump            : OUT std_logic;
      jr              : OUT std_logic;
      bne   	      : OUT std_logic;
      extsigned       : OUT std_logic
      );
  end component;

  component Forward_Unit is
    port(
        --Register each is writing to
        MEM_addr : in std_logic_vector(4 downto 0);
        WB_addr : in std_logic_vector(4 downto 0);
        EX_addr : in std_logic_vector(4 downto 0); --used for Branch and JR
        --Entire Instruction From Stage Out
        IDEX_inst : in std_logic_vector(31 downto 0);
        IFID_inst : in STD_LOGIC_VECTOR(31 downto 0);
        EXMEM_inst : in std_logic_vector(31 downto 0);
        --RegWrite toggle
        RW_MEM : in std_logic;
        RW_WB : in std_logic;
        --Used for alu input mux
        rt_out : out std_logic_vector(1 downto 0); 
        rs_out : out std_logic_vector(1 downto 0);
        --Dmem Data input mux
        sw_hazard_out : out STD_LOGIC_VECTOR(1 downto 0);
        --Decode Mux
        decode_rs : out STD_LOGIC_VECTOR(1 downto 0); 
        decode_rt : out STD_LOGIC_VECTOR(1 downto 0);
        --Control Signals
        insType : in std_logic;
        branch : in std_logic;
        jr : in std_logic
    );
    end component;


    component Hazard_Detection is
      port(
        --Main Instruction From ID stage that is being grabbed
        ID_inst : in std_logic_vector(31 downto 0);
    
        --These are never both activated off the top of my head so that is why we need both
        jump : in std_logic;
        jr : in std_logic;
        branch : in std_logic;
        iCLK : in std_logic;
        bConditional : in std_logic;
    
        --WriteBack Signal in Each Stage
        wb_MEM : in std_logic_vector(4 downto 0);
        wb_EX  : in std_logic_vector(4 downto 0);
    
        --MemToReg signal in each stage
        memToReg_MEM : in std_logic;
        memToReg_EX  : in std_logic; 
    
        --Stage Control
        flush  : out std_logic;
        stall  : out std_logic
      );
    end component;

    --32 bit mux
  component mux2t1_N is
    generic(N : integer := 32);
    port(
      i_S : in STD_LOGIC;
      i_D0 : in STD_LOGIC_VECTOR(N-1 downto 0);
      i_D1 : in STD_LOGIC_VECTOR(N-1 downto 0);
      o_O : out STD_LOGIC_VECTOR(N-1 downto 0)
    );
  end component;

  component mux3t1_32 is
    port(i_S          : in std_logic_vector(1 downto 0);
         i_D0         : in std_logic_vector(31 downto 0);
         i_D1         : in std_logic_vector(31 downto 0);
         i_D2         : in std_logic_vector(31 downto 0);
         o_O          : out std_logic_vector(31 downto 0)
         );
  end component;

  component mux4t1_32 is
    port(i_S          : in std_logic_vector(1 downto 0);
         i_D0         : in std_logic_vector(31 downto 0);
         i_D1         : in std_logic_vector(31 downto 0);
         i_D2         : in std_logic_vector(31 downto 0);
         i_D3         : in std_Logic_vector(31 downto 0);
         o_O          : out std_logic_vector(31 downto 0)
         );
  end component;

  --extender used for immediates
  component extender is
    port(
      input_16bit    : in  std_logic_vector(15 downto 0);  
        mode         : in  std_logic;
        output_32bit : out std_logic_vector(31 downto 0)
    );
  end component;

  --Stages for Pipeline
  component IFID is
    port(
        clk        : in  STD_LOGIC;
        i_RST      : in  STD_LOGIC;
        reg_write  : in  STD_LOGIC;
        write_adder_data : in  STD_LOGIC_VECTOR (31 downto 0);
        write_imem_data : in  STD_LOGIC_VECTOR (31 downto 0);
        read_adder_data : out STD_LOGIC_VECTOR (31 downto 0);
        read_imem_data : out STD_LOGIC_VECTOR (31 downto 0);
        stall : in STD_LOGIC;
        flush : in STD_LOGIC;
        halt : in std_logic
    );
  end component;

  component IDEX is
    port(
        clk        : in  STD_LOGIC;
        i_RST      : in  STD_LOGIC;
        reg_write  : in  STD_LOGIC;
        write_regout1_data : in  STD_LOGIC_VECTOR (31 downto 0);
        write_regout2_data : in  STD_LOGIC_VECTOR (31 downto 0);
        write_signextension_data : in  STD_LOGIC_VECTOR (31 downto 0);
        write_alucontrol_data : in STD_LOGIC_VECTOR(3 downto 0);
        write_currentInstruction_data : in STD_LOGIC_VECTOR(31 downto 0);
        write_nextAddr_data : in STD_LOGIC_VECTOR(31 downto 0);
        read_regout1_data : out STD_LOGIC_VECTOR (31 downto 0);
        read_regout2_data : out STD_LOGIC_VECTOR (31 downto 0);
        read_signextension_data : out STD_LOGIC_VECTOR (31 downto 0);
        read_alucontrol_data : out STD_LOGIC_VECTOR(31 downto 0);
        read_currentInstruction_data : out STD_LOGIC_VECTOR(31 downto 0);
        read_nextAddr_data : out STD_LOGIC_VECTOR(31 downto 0);
        halt  	   : in std_logic
    );
  end component;

  component MEMWB is
    port(
        clk        : in  STD_LOGIC;
        i_RST      : in  STD_LOGIC;
        reg_write  : in  STD_LOGIC;
        write_dmem_data : in  STD_LOGIC_VECTOR (31 downto 0);
        write_alu_data : in  STD_LOGIC_VECTOR (31 downto 0);
        write_wrMuxO_data : in STD_LOGIC_VECTOR(31 downto 0);
        write_nextAddr_data : in STD_LOGIC_VECTOR(31 downto 0);
        write_halt_data : in STD_LOGIC_VECTOR(31 downto 0);
        read_dmem_data : out STD_LOGIC_VECTOR (31 downto 0);
        read_alu_data : out STD_LOGIC_VECTOR (31 downto 0);
        read_wrMuxO_data : out STD_LOGIC_VECTOR(31 downto 0);
        read_nextAddr_data : out STD_LOGIC_VECTOR(31 downto 0);
        read_halt_data : out STD_LOGIC_VECTOR(31 downto 0);
        halt  	   : in std_logic
    );
  end component;

  component EXMEM is
    port(
        clk        : in  STD_LOGIC;
        i_RST      : in  STD_LOGIC;
        reg_write  : in  STD_LOGIC;
        write_alu_data : in  STD_LOGIC_VECTOR (31 downto 0);
        write_aluSrc_data : in  STD_LOGIC_VECTOR (31 downto 0);
        write_wrMuxO_data : in STD_LOGIC_VECTOR(4 downto 0);
        write_nextAddr_data : in STD_LOGIC_VECTOR(31 downto 0);
        write_reg2out_data : in STD_LOGIC_VECTOR(31 downto 0);
        write_currentInstruction_data : in STD_LOGIC_VECTOR(31 downto 0);
        read_alu_data : out STD_LOGIC_VECTOR (31 downto 0);
        read_aluSrc_data : out STD_LOGIC_VECTOR (31 downto 0);
        read_wrMuxO_data : out STD_LOGIC_VECTOR(31 downto 0);
        read_nextAddr_data : out STD_LOGIC_VECTOR(31 downto 0);
        read_reg2out_data : out STD_LOGIC_VECTOR(31 downto 0);
        read_currentInstruction_data : out STD_LOGIC_VECTOR(31 downto 0);
        halt  	   : in std_logic
    );
  end component;

  component IDEX_CntrlSignals is
    Port (
        clk        : in  STD_LOGIC;
        i_RST      : in  STD_LOGIC;
        reg_write  : in  STD_LOGIC;
        --control signals going in 
        jump_in : in STD_LOGIC;
        regDst_in : in STD_LOGIC;
        ALUsrc_in : in STD_LOGIC;
        memToReg_in : in STD_LOGIC;
        regWrite_in : in STD_LOGIC;
        memRead_in : in STD_LOGIC;
        memWrite_in : in STD_LOGIC;
        branch_in : in STD_LOGIC;
        brlShftDir_in : in STD_LOGIC;
        lui_in : in STD_LOGIC;
        bSigned_in : in STD_LOGIC;
        jr_in : in STD_LOGIC;
        bne_in : in STD_LOGIC;
        extsigned_in : in STD_LOGIC;

        --control signals passing through out
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
        jr : out STD_LOGIC;
        bne : out STD_LOGIC;
        extsigned : out STD_LOGIC;
        halt  	   : in std_logic
    );
end component;
component EXMEM_CntrlSignals is
  Port (
      clk        : in  STD_LOGIC;
      i_RST      : in  STD_LOGIC;
      reg_write  : in  STD_LOGIC; --always 1
      --control signals going in 
      jalMux_in : in STD_LOGIC;
      jump_in : in STD_LOGIC;
      memToReg_in : in STD_LOGIC;
      regWrite_in : in STD_LOGIC;
      memRead_in : in STD_LOGIC;
      memWrite_in : in STD_LOGIC;
      branch_in : in STD_LOGIC;
      bSigned_in : in STD_LOGIC;
      jr_in : in STD_LOGIC;
      bne_in : in STD_LOGIC;
      extsigned_in : in STD_LOGIC;

      --control signals passing through out
      jump : OUT STD_LOGIC;
      jalMux : OUT STD_LOGIC;
      memToReg : OUT STD_LOGIC;
      regWrite : OUT STD_LOGIC;
      memRead : OUT STD_LOGIC;
      memWrite : OUT STD_LOGIC;
      branch : OUT STD_LOGIC;
      bSigned : OUT STD_LOGIC;
      jr : out STD_LOGIC;
      bne : out STD_LOGIC;
      extsigned : out STD_LOGIC;
      halt  	   : in std_logic
  );
end component;
component  MEMWB_CntrlSignals is
  Port (
      clk        : in  STD_LOGIC;
      i_RST      : in  STD_LOGIC;
      reg_write  : in  STD_LOGIC; --always 1
      jalMux_in : in STD_LOGIC;
      memToReg_in : in STD_LOGIC;
      regWrite_in : in STD_LOGIC;
      memRead_in : in STD_LOGIC;
      memWrite_in : in STD_LOGIC;
      jalMux : OUT STD_LOGIC;
      memToReg : OUT STD_LOGIC;
      regWrite : OUT STD_LOGIC;
      memRead : OUT STD_LOGIC;
      memWrite : OUT STD_LOGIC;
      halt  	   : in std_logic
  );
end component;

begin

  -- TODO: This is required to be your final input to your instruction memory. This provides a feasible method to externally load the memory module which means that the synthesis tool must assume it knows nothing about the values stored in the instruction memory. If this is not included, much, if not all of the design is optimized out because the synthesis tool will believe the memory to be all zeros.
  with iInstLd select
    s_IMemAddr <= s_NextInstAddr when '0',
      iInstAddr when others;


  IMem: mem
    generic map(ADDR_WIDTH => ADDR_WIDTH,
                DATA_WIDTH => N)
    port map(clk  => iCLK,
             addr => s_IMemAddr(11 downto 2),
             data => iInstExt,
             we   => iInstLd,
             q    => s_Inst);
  
  DMem: mem
    generic map(ADDR_WIDTH => ADDR_WIDTH,
                DATA_WIDTH => N)
    port map(clk  => iCLK,
             addr => s_DMemAddr(11 downto 2),
             data => s_DMemData,
             we   => s_DMemWr,
             q    => s_DMemOut);

  -- TODO: Ensure that s_Halt is connected to an output control signal produced from decoding the Halt instruction (Opcode: 01 0100)
  -- TODO: Ensure that s_Ovfl is connected to the overflow output of your ALU

  -- TODO: Implement the rest of your processor below this comment! 

  --DMEM signal assigns from stages
  s_DMemWr <= s_EXMEMCS_memWrite_out;

  s_DMemData <= s_EXMEM_reg2_out;

  s_DMemAddr <= s_EXMEM_alu_out;


  RegFile: mipsreg
    port map(
      clk => iCLK,
      i_RST => iRST,
      reg_write => s_RegWr,
      read_reg1 => s_IFID_imem_out(25 downto 21),
      read_reg2 => s_IFID_imem_out(20 downto 16),
      write_reg => s_RegWrAddr,
      write_data => s_RegWrData,
      read_data1 => s_ReadData1, 
      read_data2 => s_ReadData2,
      halt => s_Halt
    );

    --Current Data that is being written bypass if write and read addr are the same [Works in combination with forward mux for branch]
    s_Read1Bypass <= s_RegWrData when s_RegWrAddr = s_IFID_imem_out(25 downto 21) else s_ReadData1;
    s_Read2Bypass <= s_RegWrData when s_RegWrAddr = s_IFID_imem_out(20 downto 16) and s_RegWr = '1' else s_ReadData2;

    --TODO LOOK HERE
    --Checking if reg reads are equal for branch
    s_regIsEqual <= '1' when s_FU_IDRS_out = s_FU_IDRT_out else '0';

    --TODO LOOK HERE
    --Logic for Branches [In ID Stage]
    process(s_branch, s_regIsEqual, s_bne)
    begin
      s_BranchAndZero <= (((s_branch and s_regIsEqual) and (not s_bne)) or (s_bne and s_branch and (not s_regIsEqual)));
    end process;



  --ALU 
  ALUunit: ALU
    port map(
      A => s_ALUin_A,
      B => s_ALUin_B,
      Control => s_IDEX_alucontrol_out(3 downto 0),
      Result => oALUOut,
      addsubsigned => s_IDEXCS_bSigned_out,
      Overflow => s_Ovfl, --Required above
      CarryO => open, --DONT THINK THIS IS NEEDED 
      lui_in => s_IDEX_currentInstruction_out(15 downto 0),
      repl_in => s_IDEX_currentInstruction_out(23 downto 16),
      shamt => s_IDEX_currentInstruction_out(10 downto 6),
      Zero => s_Zero,
      jalmux_o => s_jalMux,
      bne => s_IDEXCS_bne_out
    );

    --Control Unit
  CU: ControlUnitTest
    port map(
      OpCode => s_IFID_imem_out(31 downto 26),
      Funct => s_IFID_imem_out(5 downto 0),
      ALUControlBits => s_ALUControlBits,
      ALUsrc => s_ALUsrc,
      memToReg => s_memToReg,
      regWrite => s_WB,
      memRead => s_memRead,
      memWrite => s_memWrite,
      branch => s_branch,
      brlShftDir => open,
      lui => open,
      bSigned => s_bSigned,
      regDst => s_regDst,
      jump => s_jump,
      jr => s_jr,
      bne => s_bne,
      extsigned => s_extsigned
    );

    --TODO Make sure all of these signals are from their correct stages
    ForwardUnit: Forward_Unit
     port map(
        EX_addr => s_wrMuxO,
        MEM_addr => s_EXMEM_wrMuxO_out(4 downto 0),
        WB_addr => s_RegWrAddr,
        IDEX_inst => s_IDEX_currentInstruction_out,
        IFID_inst => s_IFID_imem_out,
        EXMEM_inst => s_EXMEM_currentInstruction_out,
        RW_MEM => s_EXMEMCS_regWrite_out,
        RW_WB => s_RegWr,
        rt_out => s_FU_rt,
        rs_out => s_FU_rs,
        insType => s_IDEXCS_ALUsrc_out,
        sw_hazard_out => s_FU_SW,
        branch => s_branch,
        jr => s_jr,
        decode_rs => s_FU_IDRS,
        decode_rt => s_FU_IDRT
      );

      --TODO broken jump branch??
      HazardDetection: Hazard_Detection
       port map(
          ID_inst => s_IFID_imem_out,
          jump => s_jump,
          jr => s_jr,
          branch => s_branch,
          iCLK => iCLK,
          wb_MEM => s_EXMEM_wrMuxO_out(4 downto 0),
          wb_EX => s_wrMuxO,
          memToReg_MEM => (s_EXMEMCS_memToReg_out), --TODO LOOK HERE
          memToReg_EX => (s_IDEXCS_memToReg_out),
          flush => s_HD_flush,
          stall => s_HD_stall,
          bConditional => s_BranchAndZero
      );

    decodeSrcRsMux: mux4t1_32
     port map(
        i_S => s_FU_IDRS, --25 downto 21
        i_D0 => s_Read1Bypass,
        i_D1 => s_EXMEM_alu_out,
        i_D2 => s_RegWrData,
        i_D3 => oALUOut,
        o_O => s_FU_IDRS_out
    );

    decodeSrcRtMux: mux4t1_32
     port map(
        i_S => s_FU_IDRT, --20 downto 16
        i_D0 => s_Read2Bypass,
        i_D1 => s_EXMEM_alu_out,
        i_D2 => s_RegWrData,
        i_D3 => oALUOut, --Straight from IDEX
        o_O => s_FU_IDRT_out
    );

    --ALU SOURCE MUX'S [Select is from Forward Unit]
    aluSrcAMux: mux3t1_32
      port map(
           i_S => s_FU_rs,        
           i_D0 => s_IDEX_reg1_out,
           i_D1 =>  s_RegWrData, 
           i_D2 => s_EXMEM_alu_out,
           o_O => s_ALUin_A
      );

    aluSrcBMux: mux3t1_32
      port map(
          i_S => s_FU_rt,        
          i_D0 => s_aluSrcMuxO,
          i_D1 => s_RegWrData,
          i_D2 => s_EXMEM_alu_out,
          o_O => s_ALUin_B
      );

      swHazardMux: mux3t1_32
       port map(
          i_S => s_FU_SW,
          i_D0 => s_IDEX_reg2_out,
          i_D1 => s_EXMEM_alu_out,
          i_D2 => s_MEMWB_alu_out,
          o_O => s_SW_takeover
      );

    aluSrcImm: mux2t1_N
      generic map(N => 32)
        port map(
          i_S => s_IDEXCS_ALUsrc_out,        
          i_D0 => s_IDEX_reg2_out,     
          i_D1 => s_IDEX_signextension_out,       
          o_O => s_aluSrcMuxO
        );

    aluResultMux: mux2t1_N
    generic map(N => 32)
    port map(
      i_S => s_MEMWBCS_memToReg_out,
      i_D0 => s_MEMWB_alu_out,
      i_D1 => s_MEMWB_dmem_out,
      o_O => aluResultMuxO
    );

    wrMux: mux2t1_N
    generic map(N => 5)
    port map(
      i_S => s_IDEXCS_regDst_out,
      i_D0 => s_IDEX_currentInstruction_out(20 downto 16),
      i_D1 => s_IDEX_currentInstruction_out(15 downto 11),
      o_O => s_wrMuxCorrected 
    );
    s_wrMuxO <= s_wrMuxCorrected when s_IDEXCS_branch_out = '0' else "00000";

    immExtender: extender
    port map(
      input_16bit => s_IFID_imem_out(15 downto 0),
      mode => s_extsigned,
      output_32bit => s_immExt
    );

    fetchlogic: fetch
      port map(
          iCLK => iCLK,
          iJumpAddr => s_IFID_imem_out,
          zbAnd => s_BranchAndZero,
          iRST => iRST,
          iJ =>  s_jump,
          iLOAD => '1',
          iExtender => s_immExt,
          oA => s_NextInstAddr,
          jr => s_jr,
          jrRead => s_FU_IDRS_out,
          halt => ((imemHalt or s_HD_stall) and (not(s_jump)))
      );

      jalWr: mux2t1_N
      generic map(N => 5)
      port map(
        i_S => s_MEMWBCS_jalMux_out, 
        i_D0 => s_MEMWB_wrMuxO_out(4 downto 0),
        i_D1 => "11111",
        o_O => s_FU_branchCorrect
      );
      --TODO LOOK HERE
      --This sets the write address to 0 when it is a branch instruction, IDK why I needed this but I did? Maybe rt or rs or something was reading this regardless if it was a branch
      s_RegWrAddr <= s_FU_branchCorrect when not(s_MEMWB_halt_out(31 downto 26) = "000100") else "00000"; 

      jalData: mux2t1_N
      generic map(N => 32)
      port map(
        i_S => s_MEMWBCS_jalMux_out, 
        i_D0 => aluResultMuxO, --good
        i_D1 => s_MEMWB_nextAddr_out,
        o_O => s_RegWrData
      );

      IFID_stage: IFID
      port map(
        clk => iCLK,
        i_RST => iRST,
        reg_write => '1',
        write_adder_data => std_logic_vector(unsigned(s_NextInstAddr) + unsigned(s_Constant)),
        write_imem_data => s_Inst,
        read_adder_data => s_IFID_adder_out,
        read_imem_data => s_IFID_imem_out,
        stall => s_HD_stall, 
        flush => s_HD_flush,
        halt => s_Halt
      );

      --TODO Make sure this is enough, might need more to be zeroed
      --Setting IDEX Inputs to 0 when stall

      s_HD_aluCode <= s_ALUControlBits when s_HD_stall = '0' else "0000";
      s_HD_MW <= s_memWrite when s_HD_stall = '0' else '0';
      s_HD_RD <= s_regDst when s_HD_stall = '0' else '0';
      s_HD_MTR <= s_memToReg when s_HD_stall = '0' else '0';
      s_HD_RW <= s_WB when s_HD_stall = '0' else '0';

      IDEX_stage: IDEX
       port map(
          clk => iCLK,
          i_RST => iRST,
          reg_write => '1',
          write_regout1_data => s_FU_IDRS_out,
          write_regout2_data => s_FU_IDRT_out,
          write_alucontrol_data => s_HD_aluCode,
          write_signextension_data => s_immExt,
          write_currentInstruction_data => s_IFID_imem_out,
          write_nextAddr_data => s_IFID_adder_out,
          read_regout1_data => s_IDEX_reg1_out,
          read_regout2_data => s_IDEX_reg2_out,
          read_signextension_data => s_IDEX_signextension_out,
          read_alucontrol_data => s_IDEX_alucontrol_out,
          read_currentInstruction_data => s_IDEX_currentInstruction_out,
          read_nextAddr_data => s_IDEX_nextAddr_out,
          halt => s_Halt
      );


      IDEX_controlSignals : IDEX_CntrlSignals
       port map(
          clk => iCLK,
          i_RST => iRST,
          reg_write => '1', --always 1
          jump_in => s_jump,
          regDst_in => s_HD_RD,
          ALUsrc_in => s_ALUsrc,
          memToReg_in => s_HD_MTR,
          regWrite_in => s_HD_RW,
          memRead_in => s_memRead,
          memWrite_in => s_HD_MW,
          branch_in => s_branch,
          brlShftDir_in => '0', --not used
          lui_in => '0', --not used
          bSigned_in => s_bSigned,
          jr_in => s_jr,
          bne_in => s_bne,
          extsigned_in => s_extsigned,
          jump => s_IDEXCS_jump_out,
          regDst => s_IDEXCS_regDst_out,
          ALUsrc => s_IDEXCS_ALUsrc_out,
          memToReg => s_IDEXCS_memToReg_out,
          regWrite => s_IDEXCS_regWrite_out,
          memRead => s_IDEXCS_memRead_out,
          memWrite => s_IDEXCS_memWrite_out,
          branch => s_IDEXCS_branch_out,
          brlShftDir => open,
          lui => open,
          bSigned => s_IDEXCS_bSigned_out,
          jr => s_IDEXCS_jr_out,
          bne => s_IDEXCS_bne_out,
          extsigned => S_IDEXCS_extsigned_out,
          halt => s_Halt
      );

      EXMEM_stage: EXMEM
       port map(
          clk => iCLK,
          i_RST => iRST,
          reg_write => '1',
          write_alu_data => oALUOut,
          write_aluSrc_data => s_aluSrcMuxO,
          write_wrMuxO_data => s_wrMuxO,
          write_nextAddr_data => s_IDEX_nextAddr_out,
          write_reg2out_data => s_SW_takeover,
          write_currentInstruction_data => s_IDEX_currentInstruction_out,
          read_alu_data => s_EXMEM_alu_out,
          read_aluSrc_data => s_EXMEM_aluSrc_out,
          read_wrMuxO_data => s_EXMEM_wrMuxO_out,
          read_nextAddr_data => s_EXMEM_nextAddr_out,
          read_reg2out_data => s_EXMEM_reg2_out,
          read_currentInstruction_data => s_EXMEM_currentInstruction_out,
          halt => s_Halt
      );

     

      EXMEM_CS: EXMEM_CntrlSignals
       port map(
          clk => iCLK,
          i_RST => iRST,
          reg_write => '1',
          jalMux_in => s_jalMux,
          jump_in => s_IDEXCS_jump_out,
          memToReg_in => s_IDEXCS_memToReg_out,
          regWrite_in => s_IDEXCS_regWrite_out,
          memRead_in => s_IDEXCS_memRead_out,
          memWrite_in => s_IDEXCS_memWrite_out,
          branch_in => s_IDEXCS_branch_out,
          bSigned_in => s_IDEXCS_bSigned_out,
          jr_in => s_IDEXCS_jr_out,
          bne_in => s_IDEXCS_bne_out,
          extsigned_in => s_IDEXCS_extsigned_out,
          jump => s_EXMEMCS_jump_out,
          jalMux => s_EXMEMCS_jalMux_out,
          memToReg => s_EXMEMCS_memToReg_out,
          regWrite => s_EXMEMCS_regWrite_out,
          memRead => s_EXMEMCS_memRead_out,
          memWrite => s_EXMEMCS_memWrite_out,
          branch => s_EXMEMCS_branch_out,
          bSigned => s_EXMEMCS_bSigned_out,
          jr => s_EXMEMCS_jr_out,
          bne => s_EXMEMCS_bne_out,
          extsigned => s_EXMEMCS_extsigned_out,
          halt => s_Halt 
      );

      MEMWB_stage: MEMWB
       port map(
          clk => iCLK,
          i_RST => iRST,
          reg_write => '1',
          write_dmem_data => s_DMemOut,
          write_alu_data => s_EXMEM_alu_out,
          write_wrMuxO_data => s_EXMEM_wrMuxO_out,
          write_nextAddr_data => s_EXMEM_nextAddr_out,
          write_halt_data => s_EXMEM_currentInstruction_out,
          read_dmem_data => s_MEMWB_dmem_out,
          read_alu_data => s_MEMWB_alu_out,
          read_wrMuxO_data => s_MEMWB_wrMuxO_out,
          read_nextAddr_data => s_MEMWB_nextAddr_out,
          read_halt_data => s_MEMWB_halt_out,
          halt => s_Halt
      );
      
      imemHalt <= '1' when s_Inst(31 downto 26) = "010100" else '0'; -- Halt the fetch immedietly
      s_Halt <= '1' when s_MEMWB_halt_out(31 downto 26) = "010100" else '0'; -- s_MEMWB_halt_out is current inst

      MEMWB_CS: MEMWB_CntrlSignals
       port map(
          clk => iCLK,
          i_RST => iRST,
          reg_write => '1',
          jalMux_in => s_EXMEMCS_jalMux_out,
          memToReg_in => s_EXMEMCS_memToReg_out,
          regWrite_in => s_EXMEMCS_regWrite_out,
          memRead_in => s_EXMEMCS_memRead_out,
          memWrite_in => s_EXMEMCS_memWrite_out,
          jalMux => s_MEMWBCS_jalMux_out,
          memToReg => s_MEMWBCS_memToReg_out,
          regWrite => s_RegWr,
          memRead => s_MEMWBCS_memRead_out,
          memWrite => s_MEMWBCS_memWrite_out,
          halt => s_Halt
      );
end structure;


