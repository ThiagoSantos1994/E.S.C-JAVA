UPDATE
    tbd_DespesaMensal
SET
    ds_NomeDespesa = :dsNomeDespesa,
    id_DetalheDespesa = :idDetalheDespesaNova
WHERE
   id_Despesa = :idDespesa
   AND id_DetalheDespesa = :idDetalheDespesa
   AND id_Funcionario = :idFuncionario



