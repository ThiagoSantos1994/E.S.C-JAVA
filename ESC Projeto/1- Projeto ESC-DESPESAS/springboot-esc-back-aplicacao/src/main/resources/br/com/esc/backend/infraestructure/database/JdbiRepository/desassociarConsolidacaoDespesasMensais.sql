UPDATE
    tbd_DespesaMensal
SET
    id_Consolidacao = 0
WHERE
   id_Despesa = :idDespesa
   AND id_Consolidacao = :idDetalheDespesa
   AND id_Funcionario = :idFuncionario