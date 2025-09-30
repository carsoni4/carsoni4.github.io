library IEEE;
use IEEE.std_logic_1164.all;

entity Hazard_Detection is
    port(
    --Main Instruction From ID stage that is being grabbed
    ID_inst : in std_logic_vector(31 downto 0);

    --These are never both activated off the top of my head so that is why we need both
    jump : in std_logic;
    jr : in std_logic;
    branch : in std_logic;
    bConditional : in std_logic;
    iCLK : in std_logic;

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

end Hazard_Detection;

architecture structural of Hazard_Detection is

--rt signals
signal ID_inst_rt :std_logic_vector(4 downto 0); 
signal ID_inst_rs : std_logic_vector(4 downto 0); 

--memToReg_EX (Mem to reg signal after wb)
signal RAW_EX : STD_LOGIC;
signal RAW_MEM : STD_LOGIC;

signal flush_internal : STD_LOGIC;
signal flush_out : STD_LOGIC;


--if rs or rt are equal to the current  reg thats geting written back to then stall [regWr on check]
begin

    ID_inst_rt <= ID_inst(20 downto 16);
    ID_inst_rs <= ID_inst(25 downto 21);

    --Flush Logic
    flush_internal <= '1'
    when ((jump = '1' and jr = '0') or (jump = '1' and (not((memToReg_EX = '1' and RAW_EX = '1') or (memToReg_MEM = '1' and RAW_MEM = '1'))))) else '1'
    when (branch = '1' and (not((memToReg_EX = '1' and RAW_EX = '1') or (memToReg_MEM = '1' and RAW_MEM = '1')))) else '0';
    --Respecting Clock
    process(iCLK)
    begin
        flush_out <= flush_internal;
    end process;


    flush <= flush_out;

    --Read After Write EX
    RAW_EX <= '1'
    when (ID_inst_rt = wb_EX and wb_EX /= "00000") else '1'
    when (ID_inst_rs = wb_EX and wb_EX /= "00000") else '0';

    --Read After Write MEM
    RAW_MEM <= '1'
    when (ID_inst_rs = wb_MEM and wb_MEM /= "00000") else '1'
    when (ID_inst_rt = wb_MEM and wb_MEM /= "00000") else '0';

    --Stall Logic
    stall <= ((((RAW_EX and memToReg_EX) or (branch and not(bConditional))))or
    (RAW_MEM and memToReg_MEM) or (branch and not(bConditional)));
    
end structural;

--Flush logic is making it so it does not flush if it is stalling for a load word hazard
--We dont want to flush the branch itself out of the pipeline :)