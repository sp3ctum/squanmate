(ns squanmate.scramblers.algsets.lin-pll-plus-1
  (:require [squanmate.scramblers.alg-trainer.scramble-generators.case-by-case-algset :as algset]))

;; Source:
;; https://docs.google.com/document/d/1sXODF-g3CowiUr1_brwyR2JpQsu6VYb-NKaYYTP1Sd8/edit
;; Credits:
;; Algs are contributed by Matt Sheerin (most algs)
;; Arrangement + images are done by Mattheo de Wit

(def ^:private even-cases
  [
   ;; L5E
   ["1. Adj-FR" "(-3,2)/(1,0)/(0,-3)/(-1,0)/(-3,0)/(1,0)/(0,3)/(-1,0)/(6,-2)"]
   ["2. Adj-BR" "(1,3) /(2,-1)/(-2,1)/(-1,-1)/(3,0)/(-2,1)/(-1,-3)"]
   ["3. Adj-BL" "(4,0)/(0,3)/(3,0)/(-1,-1)/(-2,1)/(0,-3)/(2,0)"]
   ["4. Adj-FL" "(3,2)/(1,0)/(0,-3)/(-1,0)/(3,0)/(1,0)/(0,3)/(-1,0)/(6,-2)"]
   ["5.  Opp-B" "/(3,0)/(0,3)/(1,0)/(0,-1)/(0,1)/(-1,-3)/(-3,0)/"]
   ["6. Opp-LR" "(6,-1)/(-3,0)/(4,1)/(-1,-1)/(-3,0)/(3,0)/(6,1)"]
   ["7. Cw-Swirl" "(3,-1)/(0,1)/(-3,0)/(0,-1)/(-2,1)/(-1,0)/(-3,0)/(1,0)/(5,0)"]
   ["8. Ccw-Swirl" "(-2,0)/(-1,0)/(3,0)/(1,0)/(2,-1)/(0,1)/(3,0)/(0,-1)/(6,1)"]
   ["9. W-FR" "(0,-1)/(4,1)/(2,-1)/(4,1)/(3,0)/(2,-1)/(3,1)"]
   ["10. W-BR" "(-3,-1)/(-3,0)/(4,1)/(3,0)/(-4,-1)/(-3,0)/(3,0)/(4,1)/(-1,0)"]
   ["11. W-BL" "(-2,0)/(-4,-1)/(-3,0)/(3,0)/(4,1)/(-3,0)/(-4,-1)/(3,0)/(6,1)"]
   ["12. W-FL" "(1,0)/(-1,-4)/(-2,1)/(-1,-4)/(-3,0)/(1,-2)/(-4,0)"]

   ;; Headlights in front
   ["13. Blocks-B+" "(4,0)/(2,-1)/(0,-3)/(3,0)/(-3,0)/(-2,4)/(5,0)"]
   ["14. Blocks-B-" "(-3,-1)/(1,-2)/(0,3)/(0,-3)/(3,0)/(-4,2)/(6,1)"]
   ["15. Blocks-L+" "(-5,0)/(-4,-4)/(3,0)/(0,-3)/(0,3)/(1,4)/(-4,-1)/(0,3)/(-3,1)"]
   ["16. Blocks-R-" "(6,-1)/(4,4)/(-3,0)/(3,0)/(0,-3)/(-4,-1)/(4,1)/(-3,0)/(2,0)"]
   ["17. Square-A" "(1,-3)/(0,3)/(3,0)/(3,0)/(-4,-1)/(1,4)/(0,3)/(2,-3) *"]
   ["18. Square-B" "(-2,3)/(0,-3)/(-1,-4)/(4,1)/(-3,0)/(-3,0)/(0,-3)/(-1,3) *"]
   ["19. Opp Blocks +" "(-5,3)/(-4,2)/(1,-2)/(-1,-4)/(4,1)/(0,-3)/(-4,-1)/(-3,1) *"]
   ["20. Opp Blocks -" "(0,2)/(-2,4)/(2,-1)/(1,4)/(-1,-4)/(0,3)/(1,4)/(2,0) *"]

   ;; Headlights in front
   ["21. Opp-Opp Blocks +" "(-3,-1)/(3,0)/(1,4)/(2,-1)/(4,1)/(-3,0)/(-1,2)/(0,-3)/(3,-5)"]
   ["22. Opp-Opp Blocks -" "(4,0)/(0,-3)/(-1,-4)/(1,-2)/(-4,-1)/(0,3)/(1,-2)/(3,0)/(-4,6)"]
   ["23. Fake T-perm" "(-3,-1)/(-3,0)/(-2,1)/(-1,-1)/(0,3)/(0,-3)/(0,3)/(-3,0)/(-3,-2)"]
   ["24. Opp-Fake T-perm" "(-3,-1)/(1,-2)/(0,3)/(-1,-4)/(4,1)/(-4,-1)/(1,1)/(-1,2)/(6,1)"]

   ;; Headlights on the left
   ["25. 2c-swap" "(-2,0)/(-1,-1)/(3,0)/(-3,0)/(0,3)/(-3,0)/(3,0)/(4,-2)/(2,0)"]
   ["26. Line + Square" "(6,-1)/(1,4)/(-1,-4)/(1,4)/(-4,-1)/(4,-2)/(2,0)"]
   ["27. Line + Block" "(6,-1)/(0,-3)/(1,4)/(-1,-4)/(4,1)/(-4,2)/(-3,1)"]
   ["28. 2c + H-perm" "(3,-1)/(1,1)/(3,0)/(-4,-1)/(-3,3)/(1,-2)/(-1,2)/(-3,0)/(0,-2)"]
   ["29. Fake Rb-perm" "(-3,-4)/(0,3)/(3,0)/(4,1)/(-4,-1)/(1,4)/(-1,2)/(6,-2) *"]
   ["30. Fake Gb-perm (almost)" "(0,-4)/(3,0)/(1,-2)/(-1,2)/(3,-3)/(3,0)/(6,-5)"]
   ["31. Fake Gc-perm" "(-5,0)/(0,3)/(-4,-1)/(4,-2)/(-3,0)/(0,3)/(5,-3)"]
   ["32. Fake Gd-perm" "(-5,3)/(0,-3)/(3,0)/(-4,2)/(4,1)/(0,-3)/(5,0)"]

   ;; Headlights on the left
   ["33. 2 Blocks + opp line" "(0,-1)/(4,1)/(0,3)/(-4,-1)/(1,4)/(-1,2)/(4,-2)/(-4,-3) *"]
   ["34. 2 Blocks + adj line" "(3,-1)/(4,-2)/(-3,0)/(0,3)/(0,-3)/(-1,2)/(6,1)"]
   ["35. 1-opp (a)" "(-5,0)/(0,-3)/(-1,2)/(-3,0)/(4,1)/(2,-1)/(1,4)/(3,0)/(5,6)"]
   ["36. 1-opp (b)" "(4,3)/(-4,-1)/(1,-2)/(-1,-4)/(1,-2)/(3,3)/(-1,2)/(-2,1)/(-1,-3)"]

   ;; Headlights in the back
   ["37. 2c-swap" "(4,0)/(2,-1)/(-3,3)/(3,0)/(0,-3)/(0,3)/(0,-3)/(1,1)/(-4,0)"]
   ["38. Line + Block-L" "(1,0)/(2,-4)/(4,1)/(-4,-1)/(1,4)/(-3,0)/(2,0)"]
   ["39. Line + Block-R" "(0,-1)/(4,-2)/(-4,-1)/(1,4)/(-1,-4)/(0,3)/(-3,1)"]
   ["40. 2c + H-perm" "(0,-1)/(1,-2)/(-1,-1)/(4,1)/(-4,-1)/(1,4)/(0,-3)/(-1,2)/(-3,1)"]
   ["41. 2 Blocks+" "(-3,-1)/(0,-3)/(4,1)/(-1,-4)/(0,-3)/(0,3)/(-3,0)/(4,4)/(-1,0)"]
   ["42. 2 Blocks-" "(4,0)/(3,0)/(-4,-1)/(4,1)/(0,3)/(-3,0)/(3,0)/(-4,-4)/(0,1)"]
   ["43. Fake Ga-perm" "(-3,-1)/(0,-3)/(-3,3)/(-2,1)/(-1,2)/(0,-3)/(-3,-2)"]
   ["44. Fake Gc-perm" "(4,0)/(3,0)/(3,-3)/(-1,2)/(1,-2)/(3,0)/(2,3)"]

   ;; Headlights in the back
   ["45. Opp line + block on L" "(4,3)/(-1,2)/(1,4)/(-4,-1)/(4,1)/(3,0)/(0,3)/(-1,3) *"]
   ["46. Opp line + block on R" "(3,2)/(1,-2)/(-1,-4)/(4,1)/(-4,-1)/(-3,0)/(0,-3)/(6,4) *"]
   ["47. Opp-Opp Blocks +" "(-2,0)/(-1,2)/(-2,1)/(0,-3)/(-1,2)/(-2,1)/(2,-1)/(3,0)/(6,-2)"]
   ["48. Opp-Opp Blocks -" "(6,2)/(-3,0)/(-2,1)/(2,-1)/(1,-2)/(0,3)/(2,-1)/(1,-2)/(2,0)"]

   ;; Headlights on the right
   ["49. 2c-swap" "(3,-1)/(1,1)/(-3,0)/(0,3)/(0,-3)/(0,3)/(-3,0)/(2,-4)/(-3,1)"]
   ["50. Line + Square" "(-5,0)/(-4,-1)/(1,4)/(-4,-1)/(4,1)/(2,-4)/(-3,1)"]
   ["51. Line + Block" "(-5,0)/(3,0)/(-1,-4)/(4,1)/(-4,-1)/(-2,4)/(2,0)"]
   ["52. 2c + H-perm" "(6,2)/(3,0)/(1,-2)/(-1,2)/(3,-3)/(4,1)/(-3,0)/(-1,-1)/(3,1)"]
   ["53. Fake Ra-perm" "(-2,-3)/(0,-3)/(-3,0)/(-4,-1)/(4,1)/(-1,-4)/(1,-2)/(-1,-3) *"]
   ["54. Fake Ga-perm" "(6,-1)/(-3,0)/(4,1)/(2,-4)/(3,0)/(-3,0)/(6,4)"]
   ["55. Fake Gc-perm" "(0,2)/(0,3)/(-3,0)/(4,-2)/(-4,-1)/(0,3)/(0,-5)"]
   ["56. Fake Gd-perm (almost)" "(-5,-3)/(-3,0)/(-1,2)/(1,-2)/(-3,3)/(-3,0)/(-1,0)"]

   ;; Headlights on the right
   ["57. 2 Blocks + opp line" "(1,0)/(-1,-4)/(0,-3)/(1,4)/(-1,-4)/(-2,1)/(2,-4)/(-3,-2) *"]
   ["58. 2 Blocks + adj line" "(-2,0)/(2,-4)/(3,0)/(-3,0)/(0,3)/(-2,1)/(5,0)"]
   ["59. 1-opp (a)" "(6,-1)/(3,0)/(1,-2)/(0,3)/(-4,-1)/(1,-2)/(-1,-4)/(0,-3)/(6,-5)"]
   ["60. 1-opp (b)" "(-5,3)/(2,-1)/(1,-2)/(-3,-3)/(-1,2)/(1,4)/(-1,2)/(4,1)/(2,-3)"]

   ;; Diagonal Swap
   ["61. Diag-Swap (a)" "(-2,0)/(-3,-3)/(-3,0)/(-3,0)/(3,0)/(-1,2)/(4,1)/(-4,-1)/(3,1)"]
   ["62. Diag-Swap (b)" "(3,-1)/(3,3)/(3,0)/(0,3)/(-3,0)/(-2,1)/(-4,-1)/(1,4)/(-4,0)"]
   ["63. Blocks-B+" "(-3,-1)/(-3,-2)/(-3,0)/(3,3)/(0,-3)/(3,2)/(3,1)"]
   ["64. Blocks-B-" "(4,0)/(2,3)/(0,-3)/(3,3)/(-3,0)/(-2,-3)/(-4,0)"]
   ["65. Blocks-L+" "(-2,0)/(0,3)/(-4,-1)/(1,-2)/(3,3)/(-1,-4)/(4,1)/(-4,-1)/(-3,1)"]
   ["66. Blocks-R-" "(3,-1)/(-3,0)/(4,1)/(2,-1)/(-3,-3)/(4,1)/(-4,-1)/(1,4)/(2,0)"]
   ["67. Block-L+" "(1,3)/(2,-1)/(-2,1)/(-1,2)/(0,-3)/(0,3)/(-3,0)/(1,-2)/(2,-3)"]
   ["68. Block-L-" "(6,2)/(1,-2)/(-3,0)/(0,3)/(0,-3)/(-1,2)/(-2,1)/(2,-1)/(3,-2)"]

   ;; Diagonal Swap
   ["69. Block-R+" "(1,3)/(-1,2)/(3,0)/(0,-3)/(0,3)/(1,-2)/(2,-1)/(-2,1)/(2,-3)"]
   ["70. Block-R-" "(6,2)/(-2,1)/(2,-1)/(1,-2)/(0,3)/(0,-3)/(3,0)/(-1,2)/(3,-2)"]
   ["71. No Blocks (a)" "(-2,0)/(-4,0)/(0,3)/(-3,0)/(0,-2)/(0,2)/(3,0)/(0,-3)/(4,0)/(-4,0) *"]
   ["72. No Blocks (b)" "(-5,0)/(-3,0)/(2,-4)/(-2,1)/(2,-1)/(3,0)/(1,-2)/(-1,2)/(6,-5)"]])

(def ^:private odd-cases
  [])


(def lin-pll-plus-1-algset
  (algset/->CaseByCaseAlgSet odd-cases even-cases))