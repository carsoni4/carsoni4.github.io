library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity mux2t1 is
    Port (
        i_D0    : in  STD_LOGIC;  -- Input D0
        i_D1    : in  STD_LOGIC;  -- Input D1
        i_S     : in  STD_LOGIC;  -- Select line
        o_O     : out STD_LOGIC   -- Output
    );
end mux2t1;

--logic:
--o_O = (~S*i_D0)+(S*i_D1)

architecture Structural of mux2t1 is
    --Gate Decleration
    component invg
        Port (
            i_A : in  STD_LOGIC;
            o_F : out STD_LOGIC
        );
    end component;

    component andg2
        Port (
            i_A : in  STD_LOGIC;
            i_B : in  STD_LOGIC;
            o_F : out STD_LOGIC 
        );
    end component;

    component org2
        Port (
            i_A : in  STD_LOGIC;
            i_B : in  STD_LOGIC;
            o_F : out STD_LOGIC
        );
    end component;

    -- Signals
    signal n_S : STD_LOGIC;
    signal and1_out : STD_LOGIC;
    signal and2_out : STD_LOGIC;

begin
    -- Invert Select
    U1: invg port map (
        i_A => i_S,
        o_F => n_S
    );

    -- AND gates
    U2: andg2 port map (
        i_A => i_D0,
        i_B => n_S,
        o_F => and1_out
    );

    U3: andg2 port map (
        i_A => i_D1,
        i_B => i_S,
        o_F => and2_out
    );

    --OR to combine ands 
    U4: org2 port map (
        i_A => and1_out,
        i_B => and2_out,
        o_F => o_O
    );
end Structural;
