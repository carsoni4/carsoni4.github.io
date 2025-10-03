---
layout: paper
title: CPRE 381 Project Description
---

My group was tasked with creating 32-bit MIPS-capable Pipelined, Multi-Cycle, and Single-Cycle CPUs in VHDL. This was
one of the most difficult classes I have taken at Iowa State, but it was also the class I learned the most in. Debugging
the processor was frustrating yet satisfying. Small register fixes could resolve dozens of failing tests.  
The biggest takeaway was a general understanding of how different types of CPUs work, giving me a deeper respect for modern hardware design.

<details>
  <summary style="font-size:1.1em; font-weight:bold; cursor:pointer;">Diagram Of CPU</summary>
  <div id="cpu-pdf-wrapper" style="margin-top:1rem; width:100%; height:80vh; max-height:900px;"></div>
</details>

<details>
  <summary style="font-size:1.1em; font-weight:bold; cursor:pointer;">VHDL Code Snippet</summary>
  <p>
    Below is the source VHDL for the IDEX (Instruction Decode -> Execution) Register for our pipelined CPU.
    These registers control instruction flow, handle hazards, and redirect data between stages.
  </p>
  <pre style="background-color:#2d2d2d;color:#c678dd;padding:10px;border-radius:5px;overflow-x:auto;font-family:monospace;">
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.NUMERIC_STD.ALL;

entity IDEX is
    Port (
        clk        : in  STD_LOGIC;
        i_RST      : in  STD_LOGIC;
        reg_write  : in  STD_LOGIC;
        write_currentInstruction_data : in STD_LOGIC_VECTOR(31 downto 0);
        write_regout1_data : in  STD_LOGIC_VECTOR (31 downto 0);
        write_regout2_data : in  STD_LOGIC_VECTOR (31 downto 0);
        write_signextension_data : in  STD_LOGIC_VECTOR (31 downto 0);
        write_alucontrol_data : in STD_LOGIC_VECTOR (3 downto 0);
        write_nextAddr_data : in STD_LOGIC_VECTOR(31 downto 0);
        read_currentInstruction_data : out STD_LOGIC_VECTOR(31 downto 0);
        read_regout1_data : out STD_LOGIC_VECTOR(31 downto 0);
        read_regout2_data : out STD_LOGIC_VECTOR(31 downto 0);
        read_signextension_data : out STD_LOGIC_VECTOR(31 downto 0);
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
                for i in 1 to 31 loop
                    registers(i) <= (others => '0');
                end loop;
            elsif (reg_write = '1' and halt = '0') then
                registers(to_integer(unsigned(write_regout1_reg))) <= write_regout1_data;
                registers(to_integer(unsigned(write_regout2_reg))) <= write_regout2_data;
                registers(to_integer(unsigned(write_signextension_reg))) <= write_signextension_data;
                registers(to_integer(unsigned(write_alucontrol_reg))) <= ("0000000000000000000000000000" & write_alucontrol_data);
                registers(to_integer(unsigned(write_currentInstruction_reg))) <= write_currentInstruction_data;
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

<!-- PDFObject JS -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfobject/2.2.8/pdfobject.min.js"></script>
<script>
  document.querySelectorAll('details').forEach(function(detail) {
    detail.addEventListener('toggle', function(e) {
      if (!this.open) return;
      if (this.querySelector('#cpu-pdf-wrapper')) {
        PDFObject.embed(
          "{{ '/assets/381Project/CPUDiagram.pdf' | relative_url }}",
          "#cpu-pdf-wrapper",
          { height: "100%", width: "100%", fallbackLink: "<p>This browser does not support PDFs. <a href='{{ '/assets/381Project/CPUDiagram.pdf' | relative_url }}'>Download PDF</a>.</p>" }
        );
      }
    }, { once: true });
  });
</script>

