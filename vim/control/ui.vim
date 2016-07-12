
" Open event file in small window below
below 5split $SDC_EVENTFILE
execute "normal \<C-w>k"

" Show help?
nnoremap <F1> :below vertical 50split ui/collection_events.txt<CR>

" Set marks
normal /^+Gn
for i in range(1, 4)
    for x in ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k']
        execute "normal f" . x . "m" . x
    endfor
    normal n
endfor

nnoremap <LocalLeader>c :call Clear()

function! Clear()
    let x = getline('.')[col('.') - 1]
    normal F+jlt\|5jy
    vert new
    normal p
    execute "write !control/log-event.sh -s " . x . " -e leave"
    quit!
    execute "normal `" . x
    normal jt\|5jr k
endfunction
