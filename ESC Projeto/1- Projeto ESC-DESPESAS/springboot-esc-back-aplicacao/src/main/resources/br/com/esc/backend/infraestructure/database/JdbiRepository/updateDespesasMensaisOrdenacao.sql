UPDATE
    tbd_DespesaMensal
SET
    id_OrdemExibicao = :idNovaOrdem
WHERE
   id_Despesa = :idDespesa
   AND (:idDetalheDespesa IS NULL OR id_DetalheDespesa = :idDetalheDespesa)
   AND id_OrdemExibicao = :idOrdem
   AND id_Funcionario = :idFuncionario



