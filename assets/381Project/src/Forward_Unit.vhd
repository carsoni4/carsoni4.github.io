library IEEE;
use IEEE.std_logic_1164.all;

entity Forward_Unit is
    port(
        --Instructions/WBAddress
        MEM_addr : in std_logic_vector(4 downto 0);
        WB_addr : in std_logic_vector(4 downto 0);
        EX_addr : in std_logic_vector(4 downto 0);
        IDEX_inst : in std_logic_vector(31 downto 0);
        IFID_inst : in std_logic_vector(31 downto 0);
        EXMEM_inst : in std_logic_vector(31 downto 0);

        --RegisterWriteToggles
        RW_MEM : in std_logic;
        RW_WB : in std_logic;

        --Selects for the ALU Mux
        rt_out : out std_logic_vector(1 downto 0);
        rs_out : out std_logic_vector(1 downto 0);
        sw_hazard_out : out std_logic_vector(1 downto 0);

        --Instruction Type [1 for I, 0 for R]
        insType : in std_logic;

        --Branch/JR
        branch : in std_logic;
        jr : in std_logic;
        decode_rt : out std_logic_vector(1 downto 0);
        decode_rs : out std_logic_vector(1 downto 0)
    );

end Forward_Unit;

architecture structural of Forward_Unit is
    
    --Signals rt,rs, and opcode from IDEX
    signal IDEX_rt : std_logic_vector(4 downto 0); 
    signal IDEX_rs : std_logic_vector(4 downto 0);
    signal IDEX_opcode : std_logic_vector(5 downto 0); 
    signal EXMEM_opcode : std_logic_vector(5 downto 0);

    --Signals for rt and rs from IFID
    signal IFID_rt : std_logic_vector(4 downto 0);
    signal IFID_rs : std_logic_vector(4 downto 0);

    --rs and rt signals for each stage
    signal rs_WB : std_logic_vector(1 downto 0);
    signal rt_WB : std_logic_vector(1 downto 0);
    signal rs_MEM : std_logic_vector(1 downto 0);
    signal rt_MEM : std_logic_vector(1 downto 0);

begin

    --IDEX set
    IDEX_rt <= IDEX_inst(20 downto 16);
    IDEX_rs <= IDEX_inst(25 downto 21);
    IDEX_opcode <= IDEX_inst(31 downto 26);
    --IFID set
    IFID_rt <= IFID_inst(20 downto 16);
    IFID_rs <= IFID_inst(25 downto 21);
    --EXMEM set
    EXMEM_opcode <= EXMEM_inst(31 downto 26);


    --WriteBack
    rs_WB <= "01" when RW_WB = '1' else "00";
    rt_WB <= "01" when RW_WB = '1' else "00";

    --Memory
    rs_MEM <= "10" when RW_MEM = '1' else "00";
    rt_MEM <= "10" when RW_MEM = '1' else "00";

    --Select RS comparing Instruction Decode to current Registers
    rs_out <= rs_MEM
    when IDEX_rs = MEM_addr and EXMEM_opcode /= "101011" else rs_WB
    when IDEX_rs = WB_addr else "00"; 

    --Select RT based on current Registers compared to Instruction Decode
    rt_out <= rt_MEM
    when IDEX_rt = MEM_addr and (not (insType = '1')) else rt_WB 
    when IDEX_rt = WB_addr and (not (insType = '1')) else "00";

    --Logic For SW hazard. RT is used differently
    sw_hazard_out <= "01" when (IDEX_opcode = "101011" and IDEX_rt = MEM_addr and RW_MEM = '1') else
    "10" when (IDEX_opcode = "101011" and IDEX_rt = WB_addr and RW_WB = '1') else "00";

    --Branch and Jump Register need forwards to ID
    decode_rs <= 
    "11" when (((branch = '1') or (jr = '1')) and (EX_addr = IFID_rs and EX_addr /= "00000")) else
    "01" when (((branch = '1') or (jr = '1')) and (MEM_addr = IFID_rs and MEM_addr /= "00000")) else
    "10" when (((branch = '1') or (jr = '1')) and (WB_addr = IFID_rs and WB_addr /= "00000")) else
    "00";

    decode_rt <= 
    "11" when (((branch = '1') or (jr = '1')) and (EX_addr = IFID_rt and EX_addr /= "00000")) else
    "01" when (((branch = '1') or (jr = '1')) and (MEM_addr = IFID_rt and MEM_addr /= "00000")) else 
    "10" when (((branch = '1') or (jr = '1')) and (WB_addr = IFID_rt and WB_addr /= "00000")) else
    "00";


end structural; 