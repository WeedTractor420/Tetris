Pri hre Tetris je potrebné pri generovaní herného poľa urobiť nasledujúce kroky:

1.	Vygenerovať arénu, kde budú padať tetrominá. Herné pole je v tomto prípade typu char[][], pretože pri generácii sa v konštruktore vytvorí nové pole s rozmermi rowCount a colCount a je jednoduché s takýmto polom pracovať.
2.	Následne sa v cykle prechádza a nastavujú sa hranice poľa znakmi '|', '_', a herná plocha, ktorá je hráteľná, je reprezentovaná znakom ' '.


Po každom ťahu v hre Tetris sa kontroluje, či je hra prehratá alebo vyhratá:

1.	Ak hráč vykoná pohyb tetromina dole a tetromino sa nebude môcť pohnúť už viac nadol a hra nemôže vygenerovať a položiť ďalšie tetromino na hraciu plochu, hra sa považuje za prehratú.
2.	Hra sa považuje za vyhratú, ak platí, že aktuálne skóre hráča je väčšie ako požadované skóre na vyhratie hry.
3.	Ak hra nie je ani vyhratá, ani prehratá, pokračuje sa ďalším ťahom hráča.


Ťah hráča spočíva vo výbere typu akcie:

•	Vybratie akcie pre aktuálne padajúce tetromino - ak je tetromino v stave FALLING alebo INITIALIZED, hráč môže vybrať jednu z piatich akcií (moveLeft, moveRight, moveDown, rotateLeft, rotateRight). Tetromino po vykonaní hociakej akcie prechádza do stavu FALLING. Ak sa po vykonaní hociakej akcie už nemôže pohnúť ďalej nadol, prechádza do stavu AT_BOTTOM.
•	Po každom rotovaní tetromina zároveň mení svoju rotáciu. Pri vytvorení je vždy orientované na EAST, po rotácii doprava sa mení na SOUTH, a pri rotácii doľava sa mení na NORTH atď.

