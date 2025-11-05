# ZORK ESCAPE GAME - Complete Guide

## Game Overview
You wake up locked inside a mysterious building. Your goal: **ESCAPE** before the Security Guard catches you!

**Action System:** You have 2 actions per round. The guard moves every round.

**Scoring System:** Earn points for finding items, solving puzzles, and escaping!

---

## BUILDING MAP WITH KEY ITEMS

```
                    [Dachboden/Attic]
                    Empty boxes
                    Roof Hatch (needs PISTOL + BULLETS)
                    Note: Emergency exit info
                           |
                           |
    [Bibliothek]---[Flur OG]---[Buero Chef]---[Tresor Raum]
    Keycard        Door       Note            Safe (code: 1984)
    Attic Door                                Gate Key (in safe)
    (needs CROWBAR)                           Note: Code hint
           |            |                             |
           |            |                             |
    [Keller]      [Flur EG]              [Sicherheitsraum]
    Shelves       Disguise               Pistol (in locker)
                  (on shelf)             Vent (needs CROWBAR)
           |            |                             |
           |            |                             |
 [Heizungskeller]  [Empfangshalle]      [Ueberwachungsraum]
 Code Paper        Bullets               Energy Drink
 (in toolbox)      (in drawer)           (on desk)
 Note                                 
           |            |                             |
           |            |                             |
    [Versteck]    [Kueche]              [Cafeteria]
    Chest         Crowbar               Cabinet
    (Easter Egg   (in drawer)
     location)          |                             |
           |            |                             |
           |            |                             |
           └----[Lagerraum]----------[Aussenbereich]
                Crate                 Gate (needs GATE KEY)
                Guard starts here     Trash bin
```

---

## ESCAPE ROUTES & WALKTHROUGHS

### ROUTE 1: STANDARD GATE ESCAPE (+100 points)

**Objective:** Find the gate key and unlock the main exit gate.

**Step-by-Step:**

1. **Start:** Reception Hall (Empfangshalle)
   - `search drawer` → Get **BULLETS**
   - `pickup note` → Hint about keycard location

2. **Go to Library** (Bibliothek):
   - `go east` → Flur EG
   - `go north` → Flur OG
   - `go west` → Bibliothek
   - `search books` → Get **KEYCARD** (+10 pts)

3. **Access Boss Office** (Buero Chef):
   - `go east` → Flur OG
   - `go east` → Buero Chef
   - `pickup note` → Reading hint
   - `search door` → Use KEYCARD to unlock vault door

4. **Enter Vault** (Tresor Raum):
   - `go east` → Tresor Raum
   - `pickup note` → Hint about code in boiler room

5. **Find the Code** (Heizungskeller):
   - Navigate to Heizungskeller (see map)
   - `search toolbox` → Get **CODE** paper (+10 pts)
   - Code is: **1984**

6. **Crack the Safe**:
   - Return to Tresor Raum
   - `search safe` → Enter code: `1984`
   - Get **GATE KEY** (+20 pts for cracking safe)

7. **Escape via Gate**:
   - Navigate to Aussenbereich
   - `search gate` → Use GATE KEY
   - **VICTORY!** (+100 pts) **Total: ~140 points**

---

### ROUTE 2: ATTIC ROOF ESCAPE (+100 points)

**Objective:** Break into attic and shoot the roof hatch lock.

**Step-by-Step:**

1. **Get Crowbar** (Kitchen):
   - From start: `go west` → Flur EG → `go south` → Kueche
   - `search drawer` → Get **CROWBAR** (+10 pts)

2. **Get Pistol** (Security Room):
   - Navigate to Sicherheitsraum (via Tresor Raum → south)
   - `search locker` → Get **PISTOL** (+10 pts)

3. **Get Bullets** (Reception):
   - Return to Empfangshalle
   - `search drawer` → Get **BULLETS** (+10 pts)

4. **Break into Attic** (Bibliothek):
   - Go to Bibliothek
   - `search atticdoor` → Use CROWBAR to pry open
   - `go north` → Dachboden (Attic)

5. **Shoot the Roof Hatch**:
   - `search hatch` → Use PISTOL + BULLETS
   - "You load the pistol... *BANG!*"
   - **VICTORY!** Escape via roof! (+100 pts)

**Total: ~130 points**

---

### ROUTE 3: TELEPORT EASTER EGG (+150 points)

**Objective:** Discover the secret teleport machine.

**Step-by-Step:**

1. **Navigate to Versteck** (Secret Hideout):
   - From start → Flur EG → south → Kueche → south → Lagerraum → west → Versteck
   
2. **Hide 4 Times**:
   - `hide alcove` (costs 1 action)
   - Choose "1" to stay hidden (costs 1 action) = Round ends
   - Repeat 3 more times (total: 4 rounds of hiding)

3. **Teleport Machine Appears**:
   - After 4th hide, message appears:
   - "You notice something strange in the corner..."
   - "A hidden teleport machine!"

4. **Activate Teleport**:
   - `search teleportmachine`
   - Automatic escape sequence begins
   - **VICTORY!** (+150 pts including easter egg bonus)

**Total: ~150 points** (highest solo route!)

---

### ROUTE 4: COMBAT VICTORY - SECRET ROUTE (+200 points)

**Objective:** Shoot the guard while disguised!

**Requirements:** Disguise + Pistol + Bullets (all 3 required!)

**Step-by-Step:**

1. **Get Disguise** (Flur EG):
   - `go west` → Flur EG
   - `search shelf` → Get **DISGUISE** (+10 pts)
   - `use disguise` → Activate (+50 pts)

2. **Get Pistol** (Sicherheitsraum):
   - Navigate to Sicherheitsraum
   - `search locker` → Get **PISTOL** (+10 pts)

3. **Get Bullets** (Empfangshalle):
   - Return to reception
   - `search drawer` → Get **BULLETS** (+10 pts)

4. **Wait for Guard Encounter**:
   - Move around the building
   - When guard enters your room while you have all 3 items:
   - **AUTOMATIC SEQUENCE:**
     - "*BANG!* You shoot the guard!"
     - "The guard falls unconscious!"
     - **INSTANT VICTORY!** (+200 pts)

**Total: ~280 points** (HIGHEST SCORING ROUTE!)

---

## COMPLETE ITEM LIST & LOCATIONS

| Item | Location | Room | How to Get | Points |
|------|----------|------|------------|--------|
| **Bullets** | Empfangshalle | Reception desk drawer | `search drawer` | +10 |
| **Disguise** | Flur EG | Dusty shelf | `search shelf` | +10 |
| **Keycard** | Bibliothek | Books on shelf | `search books` | +10 |
| **Crowbar** | Kueche | Kitchen drawer | `search drawer` | +10 |
| **Pistol** | Sicherheitsraum | Security locker | `search locker` | +10 |
| **Energy Drink** | Ueberwachungsraum | Control desk | `search desk` | +10 |
| **Code Paper** | Heizungskeller | Toolbox | `search toolbox` | +10 |
| **Gate Key** | Tresor Raum | Safe (code: 1984) | `search safe` + enter code | +20 |

**Powerup Usage:**
- Disguise: `use disguise` (+50 pts) - One-time guard protection
- Energy Drink: `use energydrink` (+30 pts) - Grants +1 extra action

---

## NOTES & HINTS

**5 Notes Hidden in Rooms:**

1. **Empfangshalle Note:** "Security clearance badges are kept in the boss's office"
   → Hint: Keycard is in boss-related area

2. **Buero Chef Note:** "He enjoys reading during breaks"
   → Hint: Check the library (Bibliothek)

3. **Sicherheitsraum Note:** "Kitchen inventory check overdue"
   → Hint: Check kitchen for useful items

4. **Tresor Raum Note:** "Remember: The code is hidden in the boiler room"
   → Direct hint: Safe code in Heizungskeller

5. **Dachboden Note:** "Emergency exit: Roof hatch locked with heavy padlock. Force may be necessary."
   → Hint: You can shoot the lock

**Each note gives +5 points when picked up!**

---

## GAMEPLAY TIPS & MECHANICS

### Action System
- **2 actions per round**
- Costs 1 action: `go`, `search`, `hide`, staying hidden
- FREE actions: `pickup`, `inventory`, `map`, `help`, `use`

### Guard Behavior
- Starts in Lagerraum (Storage Room)
- Moves randomly every round
- **Cannot access:** Tresor Raum (Vault), Dachboden (Attic)
- If he finds you: **GAME OVER** (unless you have disguise/gun combo)

### Hiding Strategy
- `hide <object>` in any room (costs 1 action)
- While hidden:
  - Option 1: Stay hidden (costs 1 action)
  - Option 2: Exit (FREE - no action cost)
- If guard enters while hiding: He doesn't see you! (+5 pts)

### Score Breakdown
- Find item: +10 points
- Pick up note: +5 points
- Crack safe: +20 points
- Hide from guard: +5 points per encounter
- Use disguise: +50 points
- Use energy drink: +30 points
- Escape (gate/attic): +100 points
- Escape (teleport): +150 points
- Shoot guard: +200 points

---

## OPTIMAL STRATEGY GUIDE

### Maximum Points Run (~340+ points)

**Phase 1: Collect Everything**
1. Get Bullets (Empfangshalle) +10
2. Get Disguise (Flur EG) +10
3. Get Keycard (Bibliothek) +10
4. Get Crowbar (Kueche) +10
5. Get Pistol (Sicherheitsraum) +10
6. Get Energy Drink (Ueberwachungsraum) +10
7. Get Code (Heizungskeller) +10
8. Collect all 5 notes +25
9. Crack safe for Gate Key +20

**Phase 2: Use Powerups**
10. `use disguise` +50
11. `use energydrink` +30 (gives extra action)

**Phase 3: Combat Victory**
12. Wait for guard encounter with disguise + pistol + bullets
13. Automatic shoot sequence +200
14. **INSTANT ESCAPE!**

**Total: ~395 points!**

---

## ESSENTIAL COMMANDS

### Navigation
- `go north/south/east/west` - Move between rooms
- `map` - Show building layout with your position

### Interaction
- `search <object>` - Search for items (e.g., `search drawer`)
- `pickup <note>` - Pick up and read notes
- `hide <object>` - Hide from guard (e.g., `hide desk`)
- `use <item>` - Use powerup (e.g., `use disguise`)

### Information
- `inventory` - Show items and current score
- `help` - Full command list and rules
- `quit` - Exit game

---

## ACHIEVEMENT CHECKLIST

- [ ] **Standard Escape:** Exit via main gate
- [ ] **Roof Escape:** Exit via attic roof hatch  
- [ ] **Easter Egg Hunter:** Find teleport machine
- [ ] **Combat Expert:** Shoot the guard (SECRET!)
- [ ] **Perfect Score:** 300+ points
- [ ] **Master Escapist:** 400+ points
- [ ] **Completionist:** Find all 5 notes
- [ ] **Collector:** Obtain all 8 items
- [ ] **Stealth Master:** Hide from guard 5+ times
- [ ] **Safe Cracker:** Open the vault safe

---

## TROUBLESHOOTING

**Q: I can't enter the Vault (Tresor Raum) from Boss Office**  
A: You need the KEYCARD. Search the books in Bibliothek, then `search door` in Buero Chef.

**Q: I can't open the safe!**  
A: The code is **1984**. Find the code paper in Heizungskeller toolbox.

**Q: The vent won't open!**  
A: You need the CROWBAR from the kitchen drawer.

**Q: I can't get into the attic!**  
A: Search the attic door in Bibliothek with the CROWBAR first.

**Q: The pistol won't shoot the roof hatch!**  
A: You need BOTH the pistol AND bullets in your inventory.

**Q: The guard keeps catching me!**  
A: Use the `hide` command or get the disguise powerup!

**Q: How do I shoot the guard?**  
A: You need disguise ACTIVE (`use disguise`) + pistol + bullets. The shooting happens automatically when guard enters.

---

## ESCAPE ROUTE COMPARISON

| Route | Difficulty | Items Needed | Time | Score | Special |
|-------|-----------|--------------|------|-------|---------|
| **Gate** | Medium | Keycard, Code, Gate Key | Long | +100 | Classic |
| **Attic** | Medium | Crowbar, Pistol, Bullets | Medium | +100 | Alternative |
| **Teleport** | Easy | None | Short | +150 | Easter Egg |
| **Combat** | Hard | Disguise, Pistol, Bullets | Short | +200 | SECRET! |

---

## GOOD LUCK!

Remember: 
- Use `map` to navigate
- Read all notes for hints
- Search everything you can
- Hide when the guard is near
- Think creatively - there are multiple solutions!

**Have fun escaping!**
