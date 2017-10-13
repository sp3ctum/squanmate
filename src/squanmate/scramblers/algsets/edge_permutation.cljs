(ns squanmate.scramblers.algsets.edge-permutation)

;; All edge permutation algs are from Raul Low Beattie's work! Go follow him on
;; YouTube: https://www.youtube.com/watch?v=jrXDdjaD9Ic

(def cases-with-no-parity
  (array-map
   "1. U+ / -" "/3,0/1,0/0,-3/-1,0/-3,0/1,0/0,3/-1"
   "2. - / U+" "0,-1/3,0/0,1/0,-3/0,-1/-3,0/0,1/0,3/"
   "3. U- / -" "1,0/0,-3/-1,0/3,0/1,0/0,3/-1,0/-3,0/"
   "4. - / U-" "/0,-3/0,-1/3,0/0,1/0,3/0,-1/-3,0/0,1"

   "5. Z / -" "M2 U’ M2 U M2"
   "6. - / Z" "M2 D M2 D’ M2"
   "7. H / -" "M2 U’ M2 U2 M2 U’ M2"
   "8. - / H" "M2 D M2 D2 M2 D M2"

   "9. Opp / -" "/3,3/1,0/-2,-2/2,0/2,2/0,-2/-1,-1/0,3/-3,-3/0,2/-2,-2/-1"
   "10. - / Opp" "/3,3/-1,0/2,2/-2,0/-2,-2/0,2/1,1/0,-3/-3,-3/0,2/-2,-2/-1"
   "11. Adj / -" "/-3,0/0,3/0,-3/0,3/2,0/0,2/-2,0/4,0/0,-2/0,2/-1,4/0,-3/*"
   "12. - / Adj" "/0,3/-3,0/3,0/-3,0/0,-2/-2,0/0,2/0,-4/2,0/-2,0/-4,1/3,0/*"

   "13. O+ / -" "/3,3/1,0/-2,-2/-2,0/2,2/-1,0/-3,-3/1,0/2,2/0,1"
   "14. - / O+" "/3,3/-1,0/2,2/-2,0/-2,-2/1,0/-3,-3/-2,0/2,2/0,1"
   "15. O- / -" "/3,3/1,0/-2,-2/2,0/2,2/-1,0/-3,-3/0,2/-2,-2/-1"
   "16. - / O-" "/3,3/-1,0/2,2/2,0/-2,-2/1,0/-3,-3/0,-1/-2,-2/-1"

   "17. W / -" "0,-1/1,-2/-4,0/0,3/1,0/3,-2/-4,0/-4,0/-2,2/-1,0/0,-3/*"
   "18. - / W" "/-3,0/0,-1/2,-2/0,-4/0,-4/-2,3/0,1/3,0/0,-4/-2,1/-1*"
   "19. U+ / U+" "1,0/5,-1/-3,0/1,1/-3,0/-1 *"
   "20. U- / U-" "1,0/3,0/-1,-1/3,0/-5,1/-1 *"

   "21. U+ / U-" "-2,0/5,-1/-2,1/-1,-1/3,0/-2,1/-1,-1/-2,1/-1"
   "22. U- / U+" "-2,0/3,0/-1,-1/3,0/-2,1/-1,-1/3,0/-5,1/-1"
   "23. U+ / Z" "1,0/0,3/-1,-1/1,-2/-3,0/3,0/-1,-1/-2,1/-1"
   "24. U- / Z" "1,0/0,3/-1,-1/1,-2/3,0/3,0/-1,-1/-2,1/-1"

   "25. Z / U+" "1,0/0,3/-1,-1/1,-2/0,-3/3,0/-1,-1/-2,1/-1"
   "26. Z / U-" "1,0/0,3/-1,-1/1,-2/0,3/3,0/-1,-1/-2,1/-1"
   "27. U+ / H" "0,-1/0,3/0,-3/0,3/0,-3/1,1/0,3/0,-3/0,3/0,-3/-1"
   "28. U- / H" "1,0/0,3/0,-3/0,3/0,-3/-1,-1/0,3/0,-3/0,3/0,-3/0,1"

   "29. H / U+" "0,-1/-3,0/3,0/-3,0/3,0/1,1/-3,0/3,0/-3,0/3,0/-1"
   "30. H / U-" "1,0/-3,0/3,0/-3,0/3,0/-1,-1/-3,0/3,0/-3,0/3,0/0,1"
   "31. Z / Z" "1,0/0,3/-1,-1/4,-2/-1,-1/-2,1/-1"
   "32. Z / H" "M2 U2 M2 D M2 U' M2 U' M2"

   "33. H / Z" "M2 U2 M2 U' M2 D M2 D M2"
   "34. H / H" "/-3,3/-3,3/-1,1/-3,3/-3,3/-1,1"
   "35. Opp / Opp" "M2 U2 M2"
   "36. Adj / Adj" "1,0/3,0/-1,-1/-2,1/-1"

   "37. Opp / Adj" "1,0/0,3/0,-3/0,3/-1,-1/1,-2/0,3/0,-3/-1"
   "38. Adj / Opp" "0,-1/-3,0/3,0/-2,1/-1,-1/3,0/-3,0/3,0/0,1"
   "39. Opp / O+" "M2 D' M2 D' M2"
   "40. Opp / O-" "M2 D M2 D M2"

   "41. O+ / Opp" "M2 U' M2 U' M2"
   "42. O- / Opp" "M2 U M2 U M2"
   "43. Opp / W" "1,0/5,0/0,-3/0,-1/-3,0/0,1/0,3/0,-1/-2,1/-1 *"
   "44. W / Opp" "0,-1/0,-5/3,0/1,0/0,3/-1,0/-3,0/1,0/-1,2/0,1 *"

   "45. Adj / O+" "1,0/-1,-1/0,-3/1,-2/-1,-4/1,-2/0,-3/-1,-4/0,1"
   "46. Adj / O-" "0,-1/1,4/0,3/-1,2/1,4/-1,2/0,3/1,1/-1"
   "47. O+ / Adj" "1,0/-4,-1/-3,0/-2,1/-4,-1/-2,1/-3,0/-1,-1/0,1"
   "48. O- / Adj" "0,-1/1,1/3,0/2,-1/4,1/2,-1/3,0/4,1/-1"

   "49. Adj / W" "0,-1/0,-3/1,1/0,-3/0,3/-1,-1/0,3/0,1*"
   "50. W / Adj" "1,0/3,0/-1,-1/3,0/-2,1/-1,-1/-2,1/-1 *"
   "51. O+ / W" "1,0/-4,2/3,0/1,4/3,0/-3,3/-4,-1/-2,1/-1"
   "52. O- / W" "1,0/-1,2/1,4/3,0/3,-3/-1,-4/0,-3/4,-2/-1"

   "53. W / O+" "0,-1/-2,1/-1,-4/-3,3/0,3/1,4/0,3/-4,2/0,1"
   "54. W / O-" "0,-1/4,-2/-3,0/-1,-4/3,-3/0,3/4,1/2,-1/0,1"
   "55. O+ / O+" "M2 U' M2 U' D M2 D' M2"
   "56. O- / O-" "M2 D M2 D U' M2 U M2"

   "57. O+ / O-" "M2 U' M2 U' D' M2 D M2"
   "58. O- / O+" "M2 U M2 U D M2 D' M2"
   "59. W / W" "1,0/5,-1/-3,0/1,1/0,-3/-1,-1/-2,4/-1*"))

(def cases-with-parity
  (array-map
   "60. U+ / Opp" "/3,0/-3,0/3,0/0,3/1,0/0,2/4,0/0,-4/2,0/3,-4/-3,-3/"
   "61. U- / Opp" "/-3,0/-3,0/0,3/1,0/0,2/4,0/0,-4/2,0/3,-4/0,3/0,3/"
   "62. Opp / U+" "/0,3/0,3/-3,0/0,-1/-2,0/0,-4/4,0/0,-2/4,-3/-3,0/-3,0/"
   "63. Opp / U-" "/0,-3/0,3/0,-3/-3,0/0,-1/-2,0/0,-4/4,0/0,-2/-5,0/-3,-3/"

   "64. U+ / Adj" "/-3,-3/0,1/0,-2/0,-4/-4,0/-4,0/-2,0/5,0/-3,-3/"
   "65. U- / Adj" "/3,3/-5,0/2,0/4,0/4,0/0,4/0,2/0,-1/3,3/"
   "66. Adj / U+" "/3,3/5,0/2,0/4,0/0,4/0,4/0,2/0,1/3,3/"
   "67. Adj / U-" "/-3,-3/0,-1/0,-2/0,-4/0,-4/-4,0/-2,0/-5,0/-3,-3/"

   "68. U+ / O+" "0,-1/0,-3/3,1/-3,0/3,0/1,0/-1,0/0,-4/0,4/0,-2/0,4/0,3/0,3/*"
   "69. U+ / O-" "/-3,0/3,3/3,0/-1,-2/0,2/0,-4/0,4/0,2/0,-2/1,-2/-3,-3/"
   "70. U- / O+" "/-3,-3/2,-1/0,2/-2,0/0,-4/4,0/0,-2/2,1/0,3/3,3/0,-3/"
   "71. U- / O-" "/0,3/0,3/0,-1/0,2/0,-4/0,4/1,0/-1,0/-3,0/3,0/-3,-1/0,3/0,1*"

   "72. O+ / U+" "/-3,0/-3,0/1,0/-2,0/4,0/-4,0/0,-1/0,1/0,3/0,-3/1,3/-3,0/-1*"
   "73. O+ / U-" "/0,3/-3,-3/0,-3/2,1/-2,0/4,0/-4,0/-2,0/2,0/2,-1/3,3/"
   "74. O- / U+" "/3,3/1,-2/-2,0/0,2/4,0/0,-4/2,0/-1,-2/-3,0/-3,-3/3,0/"
   "75. O- / U-" "1,0/3,0/-1,-3/0,3/0,-3/0,-1/0,1/4,0/-4,0/2,0/-4,0/-3,0/-3,0/*"

   "76. U+ / W" "/3,0/0,-4/3,0/-3,0/1, 0/-2, 0/-4,-4/0,-2/0,-1/0,4/0,-3/"
   "77. U- / W" "/3,0/0,-4/1,0/-2,0/-4,-4/0,-2/0,-1/-3,1/3,0/0,-4/3,0/0,1"
   "78. W / U+" "/0,-3/4,0/0,-1/-2,0/-4,-4/0,-2/1,0/-1,3/0,-3/4,0/0,-3/-1"
   "79. W / U-" "/0,-3/4,0/0,-3/0,3/0,-1/-2,0/-4,-4/0,-2/1,0/-4,0/3,0/"

   "80. Z / Opp" "/3,3/1,0/-2,4/2,-4/0,4/-4,-4/3,0/-3,-3/*"
   "81. Opp / Z" "/-3,-3/0,-1/-4,2/4,-2/-4,0/-2,-2/-3,0/-3,-3/*"
   "82. Z / Adj" "/-3,0/3,3/3,0/1,0/-2,4/2,-4/-1,3/0,3/-3,-3/"
   "83. Adj / Z" "/0,3/-3,-3/0,-3/0,-1/-4,2/4,-2/-3,1/-3,0/3,3/"

   "84. Z / O+" "/3,3/-1,0/2,2/-2,0/-2,-2/2,-2/-1,-1/0,3/-3,-3/0,2/-2,-2/-1"
   "85. Z / O-" "/3,3/-1,0/2,2/2,0/-2,-2/2,-2/-1,-1/0,3/-3,-3/0,-1/-2,-2/-1"
   "86. O+ / Z" "/3,3/1,0/-2,-2/-2,0/2,2/-2,2/1,1/0,-3/-3,-3/1,0/2,2/0,1"
   "87. O- / Z" "/3,3/1,0/-2,-2/2,0/2,2/-2,2/1,1/0,-3/-3,-3/-2,0/2,2/0,1"

   "88. Z / W" "/-3,0/3,3/3,0/1,0/-2,4/2,-4/-2,2/-2,1/3,3/"
   "89. W / Z" "/0,3/-3,-3/0,-3/0,-1/-4,2/4,-2/-2,2/-1,2/-3,-3/"
   "90. H / Opp" "/-3,-3/3,0/-3,-3/-2,0/-2,4/2,-4/-1,0/-3,-3/*"
   "91. Opp / H" "/-3,-3/-3,0/-3,-3/-4,0/2,-4/-2,4/1,0/-3,-3/*"

   "92. H / Adj" "/-3,0/3,3/3,0/1,0/-2,4/2,-4/0,4/-1,2/-3,-3/"
   "93. Adj / H" "/0,3/-3,-3/0,-3/0,-1/-4,2/4,-2/-4,0/-2,1/3,3/"
   "94. H / O+" "/3,3/1,0/-2,4/2,-4/2,0/2,2/-2,1/3,3/*"
   "95. H / O-" "/3,3/1,0/-2,4/2,-4/-2,2/-2,-2/3,0/-3,-3/*"

   "96. O+ / H" "/-3,-3/0,-1/-4,2/4,-2/-2,2/2,2/3,0/-3,-3/*"
   "97. O- / H" "/-3,-3/0,-1/-4,2/4,-2/0,-2/-2,-2/-1,2/-3,-3/*"
   "98. H / W" "/-3,0/3,3/3,0/1,0/-2,4/2,-4/0,4/-1,-1/-1,-1/-2,1/3,3/"
   "99. W / H" "/0,3/-3,-3/0,-3/0,-1/-4,2/4,-2/-4,0/1,1/1,1/-1,2/-3,-3/"))

(def all-cases-unordered
  (into {} (concat cases-with-no-parity
                   cases-with-parity)))
