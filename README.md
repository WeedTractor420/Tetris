**Genreovanie herného poľa:**
    Trieda GameBoard reprezentuje hraciu plochu a manipuláciu s ňou, ako sú pridávanie a mazanie tvarov. Pri vytváraní hracej plochy sa inicializujú hraničné symboly a prázdne bunky. Tieto hraničné symboly tvoria okraje hracej plochy, zatiaľ čo prázdne bunky tvoria vnútornú časť hracej plochy, na ktorú sa umiestňujú tvarové bloky. Metóda addShapeToBoard slúži na pridávanie tvarov na určité miesto na hracej ploche a metóda deleteShapeFromTheBoard na ich mazanie. Po pridávaní alebo mazaní tvarov sa môže zavolať metóda updateBoard, ktorá kontroluje a upravuje stav hracej plochy, napríklad odstránením úplných riadkov a presunom vyššie umiestnených blokov nadol.


Po každom ťahu v hre Tetris sa kontroluje, či je hra prehratá alebo vyhratá:
    Hra je vyhratá, ak hráč dosiahne určený cieľový počet bodov, čo môže byť napríklad skóre vyššie ako 10 000 bodov. Toto kritérium je implementované v metóde gameTick, kde sa kontroluje skóre hráča. Ak hráč dosiahne alebo prekročí určený počet bodov, hra je označená ako vyhratá (GameState.WON).Na druhej strane, hra je prehratá, ak sa hrací tvar dostane až na spodok hracej plochy a už nie je možné pridať ďalší tvar, pretože by zablokoval hraciu plochu. Toto je zistené v metóde gameTick, kde sa kontroluje, či aktuálny tvar dosiahol spodnú časť hracej plochy a ďalší tvar už nie je možné pridať. Ak sa tak stane, hra je označená ako prehratá (GameState.FAILED).

**Stavy tvarov:**
**Padajúci (FALLING)**: Tvar je aktívny a padá na hraciu plochu. Počas tohto stavu hráč môže ovplyvniť pohyb a rotáciu tvaru, aby ho umiestnil do požadovanej pozície.

**Na dne (AT_BOTTOM)**: Tvar sa zastavil na spodku hracej plochy, pretože už nemá možnosť voľného pádu nadol. V tomto stave tvar zablokuje svoju pozíciu a čaká na ďalšie spracovanie alebo odstránenie z hracej plochy.

**Nenainicializovaný (NOT_INITIALIZED)**: Tvar ešte nebol správne inicializovaný alebo pridelený na hraciu plochu. Tento stav je prechodný a nastáva v momente vytvorenia nového tvaru, kým sa nezačne jeho pád na hraciu plochu.

**Stavy hry:**
**Hrajeme (PLAYING)**: Hra je v aktívnom stave, hráč stále hrá a pokúša sa získať čo najviac bodov. V tomto stave hráč stále ovláda pohyb tvarov a hra pokračuje, kým hráč nedosiahne výhru, alebo prehru.

**Výhra (WON)**: Hra je vyhratá, keď hráč dosiahne určený cieľový stav, napríklad určité skóre alebo dosiahne iný definovaný cieľ. V tomto stave hráčovi býva zobrazená informácia o výhre a hra je ukončená.

**Prehra (FAILED)**: Hra je prehratá, keď hráč nedokáže pokračovať ďalej v hraní, či už preto, že hrací tvar sa nedá umiestniť na hraciu plochu bez zablokovania, alebo hráč nedosiahol cieľový stav. V tomto stave je hráčovi zobrazená informácia o prehre a hra je ukončená.

**Prechody medzi stavmi:**
**NOT_INITIALIZED** → **FALLING**: Tvar je inicializovaný a začína padať na hraciu plochu.
**FALLING** → **AT_BOTTOM**: Tvar dosiahol spodok hracej plochy a zastavil sa.
**FALLING** → **NOT_INITIALIZED**: Ak hráč v prípade zmeny tvaru stlačí klávesu pre rotáciu alebo pohyb, ale tvar ešte nespadol úplne na dno, môže sa prejsť späť do stavu **NOT_INITIALIZED**, aby mohol byť nahradený novým tvarom.


**Popis ťahov hráča:**


**Pohyb dole (S)**: Hráč môže posunúť aktuálny tvar dolu na hracej ploche.

**Pohyb vľavo (A)**: Hráč môže posunúť aktuálny tvar o jednu pozíciu doľava na hracej ploche.

**Pohyb vpravo (D)**: Hráč môže posunúť aktuálny tvar o jednu pozíciu doprava na hracej ploche.

**Rotácia doprava (R)**: Hráč môže rotovať aktuálny tvar o 90 stupňov doprava na hracej ploche.

**Rotácia doľava (L)**: Hráč môže rotovať aktuálny tvar o 90 stupňov doľava na hracej ploche.

**Návrat do menu (M)**: Hráč môže kedykoľvek počas hry návrat do hlavného menu hry.

**Ukončenie hry (Q)**: Hráč môže ukončiť hru a odísť z konzolového rozhrania.


**Možnosti v menu:**

**Start hry (1)**: Spustí novú inštanciu hry Tetris, čo zaháji hru od začiatku.

**Ohodnotenie hry (2)**: Umožňuje hráčovi ohodnotiť hru na škále od 1 do 5. Hodnotenie je potom použité na vytvorenie priemernej hodnoty hodnotenia hry.

**Komentár k hre (3)**: Hráč môže pridať komentár alebo recenziu k hre, ktorá bude zobrazená pre ostatných hráčov.

**Skóre (4)**: Zobrazí najlepšie skóre dosiahnuté hráčmi v hre Tetris.

**Zobraziť komentáre (5)**: Zobrazí komentáre a recenzie, ktoré boli pridané hráčmi k hre.

**Zobraziť moje hodnotenie (6)**: Zobrazí hodnotenie, ktoré hráč pridali k hre.

**Zobraziť priemerné hodnotenie (7)**: Zobrazí priemerné hodnotenie hry na základe hodnotení všetkých hráčov.

**Ukončiť (8)**: Ukončí aplikáciu a zavrie konzolové rozhranie.


video: https://www.youtube.com/watch?v=Hwz5EE28rGE
