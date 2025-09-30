---
layout: paper
title: COMS 3090 Project Description
---

My group was tasked with creating a 32 bit MIPS capable Pipelined, Multi-Cycle, Single Cycle CPU's in VHDL. This was
one of the most difficult classes I have taken at Iowa State, but it was the class I learned the most in. Debugging
the processor in the class was one of the most infuriating, yet satisfying processes. There were multiple times that
I would be failing over 50 tests on my CPU and I would find a single register that had been incorrectly assigned a value
and it would fix almost all of them. 
I would say the biggest takeaway from this class was the general understanding of how different types of CPU's work.
While modern CPU's are much more complex, the class allowed me to see what goes into the development of this technology 
and made me respect how far we have advanced on a hardware level.

<details>
    <summary style="font-size:1.5em; font-weight:bold;">Diagram Of CPU</summary>
    <iframe src="{{ '/assets/381Project/CPUDiagram.pdf' | relative_url }}" 
        width="100%" height="800px">
        This browser does not support PDFs. Please download the PDF to view it: 
        <a href="{{ '/assets/381Project/CPUDiagram.pdf' | relative_url }}">Download PDF</a>.
    </iframe>    
</details>

<details>
    <summary style="font-size:1.5em; font-weight:bold;">VHDL Code Snippit</summary>
    <p>
    Below is the source VHDL for the IDEX (Instruction Decode -> Execution) Register for our pipelined CPU.
    These registers were used to control the flow of instructions through the pipeline and would delay for hazards and
    redirect information where it needed to go for the next stage.
    </p>
    <pre style="background-color:#2d2d2d;color:#c678dd;padding:10px;border-radius:5px;overflow-x:auto;font-family:monospace;">    
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.NUMERIC_STD.ALL;

entity IDEX is
    Port (
        clk        : in  STD_LOGIC;
        i_RST      : in  STD_LOGIC;
        reg_write  : in  STD_LOGIC; --always 1
        write_currentInstruction_data : in STD_LOGIC_VECTOR(31 downto 0);
        write_regout1_data : in  STD_LOGIC_VECTOR (31 downto 0);
        write_regout2_data : in  STD_LOGIC_VECTOR (31 downto 0);
        write_signextension_data : in  STD_LOGIC_VECTOR (31 downto 0);
        write_alucontrol_data : in STD_LOGIC_VECTOR (3 downto 0);
        write_nextAddr_data : in STD_LOGIC_VECTOR(31 downto 0);
        read_currentInstruction_data : out STD_LOGIC_VECTOR(31 downto 0);
        read_regout1_data : out STD_LOGIC_VECTOR (31 downto 0);
        read_regout2_data : out STD_LOGIC_VECTOR (31 downto 0);
        read_signextension_data : out STD_LOGIC_VECTOR (31 downto 0);
        read_alucontrol_data : out STD_LOGIC_VECTOR(31 downto 0);
        read_nextAddr_data : out STD_LOGIC_VECTOR(31 downto 0);
        halt : in std_logic
    );
end IDEX;

architecture Behavioral of IDEX is
    type reg_array is array (0 to 31) of STD_LOGIC_VECTOR (31 downto 0);
    signal registers : reg_array := (others => (others => '0'));
    signal write_regout1_reg : std_logic_vector(4 downto 0) := "00001";
    signal write_regout2_reg : std_logic_vector(4 downto 0) := "00010";
    signal write_signextension_reg : std_logic_vector(4 downto 0) := "00011";
    signal write_alucontrol_reg : STD_LOGIC_VECTOR(4 downto 0) := "00100";
    signal write_currentInstruction_reg : STD_LOGIC_VECTOR(4 downto 0) := "00101";
    signal write_nextAddr_reg : STD_LOGIC_VECTOR(4 downto 0) := "00110";

begin
    process(clk)
    begin
        if rising_edge(clk) then
            if i_RST = '1' then
                -- Reset all registers except $0
                for i in 1 to 31 loop
                    registers(i) <= (others => '0');
                end loop;
            elsif (reg_write = '1' and halt = '0' ) then
                registers(to_integer(unsigned(write_regout1_reg))) <= write_regout1_data;
                registers(to_integer(unsigned(write_regout2_reg))) <= write_regout2_data;
                registers(to_integer(unsigned(write_signextension_reg))) <= write_signextension_data;
                registers(to_integer(unsigned(write_alucontrol_reg))) <= ("0000000000000000000000000000" & write_alucontrol_data);
                registers(to_integer(unsigned(write_currentInstruction_reg))) <=write_currentInstruction_data;
                registers(to_integer(unsigned(write_nextAddr_reg))) <= write_nextAddr_data;
            end if;
        end if;
    end process;
    read_regout1_data <= registers(to_integer(unsigned(write_regout1_reg)));
    read_regout2_data <= registers(to_integer(unsigned(write_regout2_reg)));
    read_signextension_data <= registers(to_integer(unsigned(write_signextension_reg)));
    read_alucontrol_data <= registers(to_integer(unsigned(write_alucontrol_reg)));
    read_currentInstruction_data <= registers(to_integer(unsigned(write_currentInstruction_reg)));
    read_nextAddr_data <= registers(to_integer(unsigned(write_nextAddr_reg)));
end Behavioral;
    </pre>
</details>


    
