#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define DIM 50
typedef struct albero{
    int nodo;
    struct albero* left;
    struct albero* right;
    struct albero* padre;
    int* array;
    int length;
    char colore;
}albero_t;
typedef albero_t* tree;
typedef struct nodo{ 
    int dato;
    int length;
    struct nodo *next;
} nodo_t;
typedef nodo_t * Ptr_nodo;
#define INT_MAX 2147483647

void aggiungistazione(tree*,tree*, int, int*,tree);
void aggiungiauto(tree*,tree*, int, int, int*, tree);
void demoliscistazione(tree*, int, tree);
Ptr_nodo inseriscihead(Ptr_nodo, int);
Ptr_nodo inseriscicoda(Ptr_nodo, int);
void rottamaauto(tree*, int, int,tree);
void stampafinale(Ptr_nodo);
void pianificapercorso(tree,int,int,tree);
void inavanti(tree, tree, tree);
void inindietro(tree, tree, tree);
Ptr_nodo inserisciinordine(Ptr_nodo, int);
tree inseriscirbtree(tree*,int, tree);
void rbinsertfixup(tree*, tree, tree);
void leftrotate(tree*,tree, tree);
void rightrotate(tree*,tree,tree);
tree minimumtree(tree,tree);
tree maximumtree(tree,tree);
tree searchtree(tree*, tree, int);
void treedelete(tree*,tree,tree);
tree succtree(tree,tree);
tree prectree(tree,tree);
int maxarray(int*,int);
void transplant (tree*,tree,tree,tree);
Ptr_nodo rimuovilista(Ptr_nodo, int, int*);
void visita(tree,tree);
void scorriarray(int*,int);

int main(){
    char stringa[DIM+1];
    int i, dimaggiunta, kmstazione, autmacchina, stazuno, stazdue, save;
    int* flag;
    tree root,tnil,last,curr;
    tnil=malloc(sizeof(nodo_t));
    tnil->colore='b';
    tnil->nodo=-1;
    root=tnil;
    last=tnil;
    while((scanf("%s",stringa))==1){
        //PARTE AGGIUNGI.STAZIONE
        if(!strcmp(stringa,"aggiungi-stazione")){
            if(scanf("%d",&kmstazione)){}; //printf("Chiamerei aggiungistazione con %d",kmstazione);
            flag=malloc(sizeof(int));
            *flag=1;
            aggiungistazione(&root,&last,kmstazione,flag,tnil);
            if(*flag){
                if(scanf("%d",&dimaggiunta)){};   //printf("%d ",kmstazione); 
                for (i=0;i<dimaggiunta;i++){
                    if(scanf("%d",&autmacchina)){}; //printf("Chiamerei aggiungiauto con %d",autmacchina);
                    aggiungiauto(&root,&last,kmstazione,autmacchina,flag,tnil);
                }
            }
            else{
                if(scanf("%d",&dimaggiunta)){}
                for (i=0;i<dimaggiunta;i++){
                    if(scanf("%d",&autmacchina)){}
                }
            }
            free(flag);
        }
        if(!strcmp(stringa,"visita")){
            visita(root,tnil);
        }
        if(!strcmp(stringa,"scorriarray")){
            if(scanf("%d",&save)){};
            curr=searchtree(&root,tnil,save);
            scorriarray(curr->array,curr->length);
        }
        //PARTE AGGIUNGI.AUTO 
        if(!strcmp(stringa,"aggiungi-auto")){
            flag=malloc(sizeof(int));
            *flag=0;
            if(scanf("%d",&kmstazione)){}; 
            if(scanf("%d",&autmacchina)){};
            aggiungiauto(&root,&last,kmstazione,autmacchina,flag,tnil);
            free(flag);
        }
        //PARTE ROTTAMA.AUTO
        if(!strcmp(stringa,"rottama-auto")){
            if(scanf("%d",&kmstazione)){}; 
            if(scanf("%d",&autmacchina)){};
            rottamaauto(&root,kmstazione,autmacchina,tnil);
        }
        //PARTE DEMOLISCI.STAZIONE
        if(!strcmp(stringa,"demolisci-stazione")){
            if(scanf("%d",&kmstazione)){};
            demoliscistazione(&root,kmstazione,tnil);
        }
        //PARTE PIANIFICA.PERCORSO
        if(!strcmp(stringa,"pianifica-percorso")){
            if(scanf("%d",&stazuno)){};
            if(scanf("%d",&stazdue)){};
            pianificapercorso(root,stazuno,stazdue,tnil);
        }
    }
    return 0;
}

void aggiungistazione(tree *root, tree* last, int kmstazione, int* flag,tree tnil){
    tree tmp;
    if((*root)!=tnil){
        tmp=searchtree(root,tnil,kmstazione);
        if(tmp==tnil){
            tmp=inseriscirbtree(root,kmstazione,tnil);
            tmp->array=malloc(sizeof(int)*512);
            tmp->length=0;
            (*last)=tmp;
            printf("aggiunta\n");
        }
        else{
            (*flag)=0;
            printf("non aggiunta\n");
        }
    }
    else{
        tmp=inseriscirbtree(root,kmstazione,tnil);
        tmp->array=malloc(sizeof(int)*512);
        tmp->length=0;
        (*last)=tmp;
        printf("aggiunta\n");
    }
    return;
}
Ptr_nodo inseriscihead(Ptr_nodo head, int n){
    Ptr_nodo tmp;
    tmp = malloc(sizeof(nodo_t)); 
    if(tmp != NULL){
        tmp->dato = n;
        tmp->next = head;
        head=tmp;
    } 
    return head;
}
void aggiungiauto(tree* root, tree* last, int kmstazione,int autmacchina,int* flag, tree tnil){ 
    tree curr;
    if((*last)!=tnil&&(*last)->nodo==kmstazione){
        (*((*last)->array+(*last)->length))=autmacchina;
        ((*last)->length)++;
        if(!(*flag)){
            printf("aggiunta\n");
        }
    }
    else{
        curr=searchtree(root,tnil,kmstazione);
        if(curr!=tnil){
            (*(curr->array+curr->length))=autmacchina;
            (curr->length)++;
            if(!(*flag)){
            printf("aggiunta\n");
            }
        }
        else{
            printf("non aggiunta\n");
        }
    }
    return;
}
void demoliscistazione(tree* root, int kmstazione,tree tnil){ 
    tree tmp;
    tmp=searchtree(root,tnil,kmstazione);
    if(tmp!=tnil){
        free(tmp->array);
        treedelete(root,tmp,tnil);
        printf("demolita\n");
    }
    else{
        printf("non demolita\n");
    }
    return;
}
void rottamaauto(tree* root,int kmstazione, int autmacchina,tree tnil){
    int flag,i;
    tree curr;
    curr=searchtree(root,tnil,kmstazione);
    if(curr!=tnil){
        flag=0;
        for(i=0;i<curr->length;i++){
            if(*(curr->array+i)==autmacchina){
                flag=1;
                *(curr->array+i)=*(curr->array+curr->length-1);
                curr->length--;
                break;
            }
        }
        if(flag==1){
            printf("rottamata\n");
        }
        else{
            printf("non rottamata\n");
        }
    }
    else printf("non rottamata\n");
    return;
}
Ptr_nodo inseriscicoda(Ptr_nodo head, int n){
    Ptr_nodo tmp, prec;
    tmp = malloc(sizeof(nodo_t)); 
    tmp->dato = n;
    tmp->next = NULL;
    if(head == NULL){
        head = tmp;
    }
    else{
        prec=head;
        while(prec->next!=NULL){
            prec=prec->next;
        }
        prec->next=tmp;
    }
    return head;
}
void stampafinale(Ptr_nodo hmin){
    printf("%d",hmin->dato);
    hmin=hmin->next;
    while(hmin!=NULL){
        printf(" %d",hmin->dato);
        hmin=hmin->next;
    }
    printf("\n");
    return;
}
void pianificapercorso(tree root,int uno,int due,tree tnil){
    tree l,r;
    l=searchtree(&root,tnil,uno);
    r=searchtree(&root,tnil,due);
    if (uno<due){
        inavanti(l,r,tnil);
    }
    else {
        if (uno>due){
        inindietro(l,r,tnil);
        }
        else{
            printf("%d",l->nodo);
            printf("\n");
        }
    }
    return;
}
void inindietro(tree l, tree r,tree tnil){
    int max,autonomia,distanza,heretor,flag,count,useless;
    int thistotal;
    tree curr,tmp,save;
    Ptr_nodo finallist;
    flag=0;
    max=0;
    count=1;
    finallist=NULL;
    finallist=inseriscicoda(finallist,l->nodo);
    autonomia=maxarray(l->array,l->length);
    curr=prectree(l,tnil);
    distanza=l->nodo-curr->nodo;
    heretor=l->nodo-r->nodo;
    tmp=l;
    while(!flag){
        if(autonomia>=heretor){
            flag=1;
            break;
        }
        while(autonomia>=distanza){
            thistotal=(maxarray(curr->array,curr->length)+distanza);
            heretor=curr->nodo-r->nodo;
            if((thistotal>=max)){
                if((thistotal-distanza)>=heretor){
                    flag=1;
                    max=thistotal;
                    save=curr;
                }
                else{
                    max=thistotal;
                    save=curr;
                }
            }
            curr=prectree(curr,tnil);
            distanza=tmp->nodo-curr->nodo;
        }
        if(max==0){
            printf("nessun percorso\n");
            return;
        }
        finallist=inseriscicoda(finallist,save->nodo);
        count++;
        autonomia=maxarray(save->array,save->length);
        tmp=save;
        heretor=save->nodo-r->nodo;
        max=0;
        curr=prectree(save,tnil);
        distanza=save->nodo-curr->nodo;
    }
    finallist=inseriscicoda(finallist,r->nodo);
    count++;
    if(count>2){
        tree curruno,succuno,precuno;
        int precdue,succdue,currdue,autprec,autcurr;
        Ptr_nodo move;
        succuno=r;
        succdue=r->nodo;
        move=finallist;
        curruno=succtree(r,tnil);
        while(move->next->dato!=succdue){
            move=move->next;
        }
        currdue=move->dato;
        while(curruno->nodo<currdue){
            curruno=succtree(curruno,tnil);
        }
        move=finallist;
        while(move->next->dato!=currdue){
            move=move->next;
        }
        precdue=move->dato;
        precuno=succtree(curruno,tnil);
        while(precuno->nodo<precdue){
            precuno=succtree(precuno,tnil);
        }
        while (precuno!=l){
            autprec=maxarray(precuno->array,precuno->length);
            curruno=prectree(curruno,tnil);
            distanza=precuno->nodo-curruno->nodo;
            save=tnil;
            while(curruno!=succuno&&autprec>=distanza){
                autcurr=maxarray(curruno->array,curruno->length);
                if(autcurr>=curruno->nodo-succuno->nodo){
                    save=curruno;
                }
                curruno=prectree(curruno,tnil);
                distanza=precuno->nodo-curruno->nodo;
            }
            if(save!=tnil){
                useless=0;
                finallist=rimuovilista(finallist,currdue,&useless);
                finallist=inserisciinordine(finallist,save->nodo);
                currdue=save->nodo;
            }
            succdue=currdue;
            currdue=precdue;
            move=finallist;
            while(move->next->dato!=currdue){
                move=move->next;
            }
            precdue=move->dato;
            curruno=precuno;
            while(succuno->nodo<succdue){
                succuno=succtree(succuno,tnil);
            }
            while(precuno->nodo<precdue){
                precuno=succtree(precuno,tnil);
            }
        }
        autprec=maxarray(precuno->array,precuno->length);
        curruno=prectree(curruno,tnil);
        distanza=precuno->nodo-curruno->nodo;
        save=tnil;
        while(curruno!=succuno&&autprec>=distanza){
            autcurr=maxarray(curruno->array,curruno->length);
            if(autcurr>=curruno->nodo-succuno->nodo){
                save=curruno;
            }
            curruno=prectree(curruno,tnil);
            distanza=precuno->nodo-curruno->nodo;
        }
        if(save!=tnil){
            useless=0;
            finallist=rimuovilista(finallist,currdue,&useless);
            finallist=inserisciinordine(finallist,save->nodo);
        }
    }
    stampafinale(finallist);
    return;
}
void inavanti(tree l, tree r, tree tnil){
    int autonomia,distanza,flag;
    tree curr,save,tmp;
    Ptr_nodo finallist;
    flag=0;
    finallist=NULL;
    finallist=inseriscihead(finallist,r->nodo);
    curr=prectree(r,tnil);
    tmp=r;
    while(!flag){
        save=tnil;
        while(curr!=l){
            distanza=tmp->nodo-curr->nodo;
            autonomia=maxarray(curr->array,curr->length);
            if (autonomia>=distanza){
                save=curr;
            }
            curr=prectree(curr,tnil);
        }
        distanza=tmp->nodo-curr->nodo;
        autonomia=maxarray(curr->array,curr->length);
        if (autonomia>=distanza){
                flag=1;
                save=curr;
        }
        else{
            if(save!=tnil){
                tmp=save;
                curr=prectree(save,tnil);
            }
        }
        if(save!=tnil){
            finallist=inseriscihead(finallist,save->nodo);
        }
        else{
            printf("nessun percorso\n");
            return;
        }
    }
    stampafinale(finallist);
    return;
}
Ptr_nodo inserisciinordine(Ptr_nodo head, int n){ 
    if (!head || head->dato < n){
        return inseriscihead(head,n);
    }
    head->next = inserisciinordine(head->next, n); 
    return head;
}
Ptr_nodo rimuovilista(Ptr_nodo head, int n, int*flag){ 
    Ptr_nodo curr, prec, tmp;
    curr = head;
    prec = NULL; 
    while(curr && ! (*flag)){
        if(curr->dato == n){ 
            (*flag)=1;
            tmp = curr;
            curr = curr->next; 
            if(prec!=NULL)
                prec->next = curr; 
            else
                head = curr; 
            free(tmp);
        } else{
            prec=curr;
            curr = curr->next;
        } 
    }
    return head;
}
tree inseriscirbtree(tree* root, int autmacchina, tree tnil){
    tree x,y,z;
    z=malloc(sizeof(albero_t));
    z->nodo=autmacchina;
    y=tnil;
    x=(*root);
    while(x!=tnil){
        y=x;
        if (z->nodo<x->nodo){
            x=x->left;
        }
        else{
            x=x->right;
        }
    }
    z->padre=y;
    if(y==tnil){
        *(root)=z;
    }
    else if(z->nodo<y->nodo){
        y->left=z;
    }
    else{
        y->right=z;
    }
    z->left=tnil;
    z->right=tnil;
    z->colore='r';
    rbinsertfixup(root,z,tnil);
    return z;
}
void rbinsertfixup(tree* root, tree z,tree tnil){
    tree y;
    if(z->padre->padre!=tnil){
        while(z->padre->colore=='r'){
            if(z->padre==z->padre->padre->left){
                y=z->padre->padre->right;
                if(y->colore=='r'){
                    z->padre->colore='b';
                    y->colore='b';
                    z->padre->padre->colore='r';
                    z=z->padre->padre;
                }
                else{
                    if(z==z->padre->right){
                        z=z->padre;
                        leftrotate(root,z,tnil);
                    }
                    z->padre->colore='b';
                    z->padre->padre->colore='r';
                    rightrotate(root,z->padre->padre,tnil);
                }
            }
            else{
                y=z->padre->padre->left;
                if(y->colore=='r'){
                    z->padre->colore='b';
                    y->colore='b';
                    z->padre->padre->colore='r';
                    z=z->padre->padre;
                }
                else{
                    if(z==z->padre->left){
                        z=z->padre;
                        rightrotate(root,z,tnil);
                    }
                    z->padre->colore='b';
                    z->padre->padre->colore='r';
                    leftrotate(root,z->padre->padre,tnil);
                }
            }
        }
        (*root)->colore='b';
    }
    else{
        return;
    }
}
void leftrotate(tree* root,tree x,tree tnil){
    tree y;
    y=x->right;
    x->right=y->left;
    if(y->left!=tnil){
        y->left->padre=x;
    }
    y->padre=x->padre;
    if(x->padre==tnil){
        (*root)=y;
    }
    else if(x==x->padre->left){
        x->padre->left=y;
    }
    else{
        x->padre->right=y;
    }
    y->left=x;
    x->padre=y;
}
void rightrotate(tree* root,tree x,tree tnil){
    tree y;
    y=x->left;
    x->left=y->right;
    if(y->right!=tnil){
        y->right->padre=x;
    }
    y->padre=x->padre;
    if(x->padre==tnil){
        (*root)=y;
    }
    else if(x==x->padre->right){
        x->padre->right=y;
    }
    else{
        x->padre->left=y;
    }
    y->right=x;
    x->padre=y;
}
tree searchtree(tree* root, tree tnil, int value){
    tree curr;
    curr=(*root);
    while(curr!=tnil&&value!=curr->nodo){
        if(value<curr->nodo){
            curr=curr->left;
        }
        else{
            curr=curr->right;
        }
    }
    return curr;
}
tree maximumtree(tree root,tree tnil){
    if(root==tnil){
        return tnil;
    }
    while(root->right!=tnil){
        root=root->right;
    }
    return root;
}
tree minimumtree(tree root,tree tnil){
    if(root==tnil){
        return tnil;
    }
    while(root->left!=tnil){
        root=root->left;
    }
    return root;
}
void treedelete(tree* root,tree z,tree tnil){
    tree y;
    if(z->left==tnil){
        transplant(root,z,z->right,tnil);
    }
    else{
        if(z->right==tnil){
            transplant(root,z,z->left,tnil);
        }
        else{
            y=minimumtree(z->right,tnil);
            if(y!=z->right){
                transplant(root,y,y->right,tnil);
                y->right=z->right;
                y->right->padre=y;
            }
            transplant(root,z,y,tnil);
            y->left=z->left;
            y->left->padre=y;
        }
    }
    free(z);
}
void transplant(tree* root,tree u,tree v,tree tnil){
    if(u->padre==tnil){
        (*root)=v;
    }
    else{
            if(u==u->padre->left){
                u->padre->left=v;
            }
            else{
                u->padre->right=v;
            }
    }
    if(v!=tnil){
        v->padre=u->padre;
    }
}
tree prectree(tree nodo,tree tnil){
    tree y;
    if(nodo->left!=tnil){
        return maximumtree(nodo->left,tnil);
    }
    else{
        y=nodo->padre;
        while(y!=tnil&&nodo==y->left){
            nodo=y;
            y=y->padre;
        }
        return y;
    }
}
tree succtree(tree nodo,tree tnil){
    tree y;
    if(nodo->right!=tnil){
        return minimumtree(nodo->right,tnil);
    }
    else{
        y=nodo->padre;
        while(y!=tnil&&nodo==y->right){
            nodo=y;
            y=y->padre;
        }
        return y;
    }
}
int maxarray(int* idx, int length){
    int i,max;
    max=0;
    for(i=0;i<length;i++){
        if(*(idx+i)>max){
            max=*(idx+i);
        }
    }
    return max;
}
void visita(tree root,tree tnil){
    if (root!=tnil){
        printf("%d\n",root->nodo);
        printf("Figlio sinistro:\n");
        visita(root->left,tnil);
        printf("Figlio destro:\n");
        visita(root->right,tnil);
    }
}
void scorriarray(int* idx,int length){
    int i;
    for(i=0;i<length;i++){
        printf("%d-",*(idx+i));
    }
    printf("\n");
    return;
}