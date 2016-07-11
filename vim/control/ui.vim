
" Open event file in small window below
below 5split $SDC_EVENTFILE
execute "normal \<C-w>k"

" Show help?
nnoremap <F1> :below vertical 50split ui/collection_events.txt<CR>

nnoremap <LocalLeader>c v"zy:w !control/log-event.sh -s <C-r>z -e leave<CR>j<C-v>5jt\|r<Space>k
"                                                                          ^ ab hier wird das zeug rausgelöscht
