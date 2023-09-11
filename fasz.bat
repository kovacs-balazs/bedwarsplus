echo "# bedwarsplus" >> README.md
git init
git git config --global --add safe.directory '*'
git add README.md
git commit -m "first commit"
git branch -M main
git remote add origin https://github.com/kovacs-balazs/bedwarsplus.git
git push -u origin main
PAUSE