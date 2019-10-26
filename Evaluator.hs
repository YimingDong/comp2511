module MinHS.Evaluator where
import qualified MinHS.Env as E
import MinHS.Syntax
import MinHS.Pretty
import qualified Text.PrettyPrint.ANSI.Leijen as PP

type VEnv = E.Env Value

data Value = I Integer
           | B Bool
           | Nil
           | Cons Integer Value
           | Fun VEnv [Char] [[Char]] Exp
           -- Others as needed
           deriving (Show,Eq)

instance PP.Pretty Value where
  pretty (I i) = numeric $ i
  pretty (B b) = datacon $ show b
  pretty (Nil) = datacon "Nil"
  pretty (Cons x v) = PP.parens (datacon "Cons" PP.<+> numeric x PP.<+> PP.pretty v)
  --pretty _ = undefined -- should not ever be used

evaluate :: Program -> Value
evaluate [Bind _ _ _ e] = evalE E.empty e
evaluate bs = evalE E.empty (Let bs (Var "main"))


evalE :: VEnv -> Exp -> Value
-- evalE = error "implement me!"
evalE e (Num n) = I n
evalE e (Con "True") = B True
evalE e (Con "False") = B False
evalE e (App (App (Con "Cons") a) b) = 
  case evalE e a of 
    I n -> Cons n (evalE e b)
    _ -> error "Not a number"
--[Bind "main" (TypeApp (TypeCon List) (TypeCon Int)) [] (App (App (Con "Cons") (Num 3)) (App (App (Con "Cons") (Num 2)) (App (App (Con "Cons") (Num 1)) (Con "Nil"))))]
evalE e (Con ("Nil")) = Nil
--(App (Prim Head) (App (App (Con "Cons") (Num 1)) (App (App (Con "Cons") (Num 2)) (Con "Nil"))))]

evalE e (App (Prim Null) exp) =
  case evalE e exp of 
    Nil -> B True
    Cons n val -> B False
    _ -> error "List only"

evalE e (App (Prim op) (Con "Nil")) = 
  case op of
    Head  -> error "The list should not be empty"
    Tail  -> error "The list should not be empty"
    _     -> error "Illegal operation"  
evalE e (App (Prim op) exp) = 
  case evalE e exp of
    I n       -> case op of 
      Neg   -> I (-n)
      fun     -> Fun e "" ["z"] (App (App (Prim fun) (Var "z")) (Num n))
    Fun fe name [] body  -> 
      case evalE fe body of
        Cons n val  -> 
          case op of 
            Head  -> I n
            Tail  -> val
            _     -> error "Illegal operation"
    Cons n val -> case op of 
      Head  -> I n
      Tail  -> val
      _     -> error "Illegal operation"
    Nil -> evalE e (App (Prim op) (Con "Nil"))
    _   -> error "Invalid parameters"
--(App (App (Prim Add) (Num 1)) (Num 2))
evalE e (App (App (Prim op) a) b) =
  case (evalE e a, evalE e b) of
    (I x,I y) ->
      case op of 
        Add -> I (x+y)
        Sub -> I (x-y)
        Mul -> I (x*y)
        Quot -> if y == 0 then error "Can't divde zero"
                else I (quot x y)
        Rem -> error "Don't know how to deal it"
        Gt -> B (x>y)
        Ge -> B (x>=y)
        Lt -> B (x<y)
        Le -> B (x<=y)
        Eq -> B (x==y)
        Ne -> B (x/=y)
        _ -> error "Undefined operation"
    _ -> error "Numbers only"    
--(Let [Bind "x" (TypeCon Int) [] (Num 1)] (Var "x"))]
--     x = 1                               in x
--[Bind "main" (TypeApp (TypeCon List) (TypeCon Int)) [] (Let [Bind "nil" (TypeApp (TypeCon List) (TypeCon Int)) [] (Con "Nil")] (Let [Bind "cons3nil" (TypeApp (TypeCon List) (TypeCon Int)) [] (App (App (Con "Cons") (Num 3)) (Var "nil"))] (Var "cons3nil")))]

--[Bind "main" (TypeCon Bool) [] (Let [Bind "lt0" (Arrow (TypeCon Int) (TypeCon Bool)) [] (Recfun (Bind "lt0" (Arrow (TypeCon Int) (TypeCon Bool)) ["x"] (If (App (App (Prim Lt) (Var "x")) (Num 0)) (Con "True") (Con "False"))))] (Let [Bind "gt1" (Arrow (TypeCon Int) (TypeCon Bool)) [] (Recfun (Bind "gt1" (Arrow (TypeCon Int) (TypeCon Bool)) ["x"] (App (App (Prim Gt) (Var "x")) (Num 1))))] (If (App (Var "lt0") (App (Prim Neg) (Num 10))) (If (App (Var "gt1") (Num 0)) (Con "False") (Con "True")) (Con "False"))))]

evalE e (Var x) = 
  case E.lookup e x of 
    Just (Fun fe _ [] body) -> evalE fe body
    Just exp -> exp
    _ -> Nil

evalE e (Let [] exp) = evalE e exp
evalE e (Let ((Bind name tp [] body):binds) exp) = evalE nenv (Let binds exp)
  where nenv = E.add e (name, (evalE e body))

evalE e (Let ((Bind name tp args body):binds) exp) = evalE nenv (Let binds exp)
  where nenv = E.add e (name, Fun e name args body)

evalE e (If condition e1 e2) =  
  case evalE e condition of 
    B True  -> evalE e e1
    B False -> evalE e e2

--(Recfun (Bind "f" (Arrow (TypeCon Int) (TypeCon Int)) ["x"] (App (App (Prim Add) (Var "x")) (Var "x"))))] (App (Var "f") (Num 1)))
evalE e (App (Var x) exp) =
  case (E.lookup e x) of 
    Just (Fun e' name args body)  -> evalF e (Fun e' name args body) exp
    Nothing -> error ("Function not in scope")
evalE e (App fun exp) = 
  case evalE e fun of 
    Fun e' name args body   -> evalF e (Fun e' name args body) exp

evalE e (Recfun (Bind name tp args body)) = Fun nenv name args body
  where nenv = E.add e (name, (Fun e name args body))


evalE e (Letrec [] exp) = evalE e exp
evalE e (Letrec ((Bind name tp [] body):xs) exp) = 
  case evalE e body of 
    Nil -> evalE e (Letrec (xs ++ [Bind name tp [] body]) exp)
    x   -> let nenv = E.add e (name, x)
            in evalE nenv (Letrec xs exp)

evalF :: VEnv -> Value -> Exp -> Value    
evalF e (Fun fe name [] body) exp = 
  case evalE nenv body of 
    Fun nenv' newName newArgs' newBody' -> evalF e (Fun nenv' newName newArgs' newBody') exp
    result -> result
    where nenv = E.add fe (name, Fun fe name [] body)

evalF e (Fun fe name args body) exp = 
  if xs == [] then evalE nenv' body --if get all arguments needed then evaluate
  else (Fun nenv' name xs body)--else wait for other arguments
  where nenv = E.add fe (name, Fun fe name args body)
        x = head args
        xs = tail args
        nenv' = E.add nenv (x, evalE e exp)--first arg