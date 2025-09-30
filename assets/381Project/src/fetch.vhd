LIBRARY IEEE;
USE ieee.std_logic_1164.ALL;
USE ieee.numeric_std.ALL;

entity fetch is 
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
end fetch;

architecture mixed of fetch is

    -- Signals
    signal s_mux1Out: std_logic_vector(31 downto 0);
    signal s_mux2Out: std_logic_vector(31 downto 0);
    signal s_regOut: std_logic_vector(31 downto 0);
    signal s_jrmuxOut : std_logic_vector(31 downto 0);

    --Mux1 signals
    signal s_Constant: std_logic_vector(31 downto 0) := x"00000004";
    signal s_D0: std_logic_vector(31 downto 0);
    signal s_D1: std_logic_vector(31 downto 0);

    --Mux2 signal
    signal s_mux2D1: std_logic_vector(31 downto 0);





    -- Register component
    component pc is
        generic(N : integer := 32);
        port (
            i_CLK : in STD_LOGIC;
            i_RST : in STD_LOGIC;
            i_D : in STD_LOGIC_VECTOR(N-1 downto 0);
            o_Q : out STD_LOGIC_VECTOR(N-1 downto 0);
            halt : in std_logic
        );
    end component;


    -- MUX component
    component mux2t1_N is
        generic (N : integer := 32);
        port(
            i_S          : in std_logic;
            i_D0         : in std_logic_vector(N-1 downto 0);
            i_D1         : in std_logic_vector(N-1 downto 0);
            o_O          : out std_logic_vector(N-1 downto 0)
        );
    end component;

begin



    reg: pc
        generic map(N => 32)
        port map(
            i_CLK => iCLK,
            i_RST => iRST,
            i_D => s_jrmuxOut,
            o_Q => s_regOut,
            halt => halt
        );


 	 oA <= s_regOut; --Tying output to reg out


        s_D0 <= (std_logic_vector(unsigned(s_regOut) + unsigned(s_Constant))); --input for mux1 d0 regOut + 4
        s_D1 <= std_logic_vector((unsigned(iExtender) sll 2) + (unsigned(s_regOut))); --mux1D1 iExtender sll 2 + (regout + 4)

    --mux for jump reg
    jrmux: mux2t1_N 
        generic map(N => 32)
        port map(
        i_S => jr,
        i_D0 => s_mux2Out,    
        i_D1 => jrRead,       
        o_O => s_jrmuxOut
        );


    mux1: mux2t1_N 
        generic map(N => 32)
        port map(
            i_S => zbAnd,
            i_D0 => s_D0,    
            i_D1 => s_D1,       
            o_O => s_mux1Out      
        );

        s_mux2D1 <= (s_regOut(31 downto 28) & iJumpAddr(25 downto 0) & "00");

    mux2: mux2t1_N 
        generic map(N => 32)
        port map(
            i_S => iJ,
            i_D0 => s_mux1Out,    
            i_D1 => s_mux2D1,    
            o_O => s_mux2Out      
        );

end mixed;
