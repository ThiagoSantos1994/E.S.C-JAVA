DELETE FROM
    tbd_ObservacaoDetalheDespesaMensal
WHERE
   id_Despesa = :idDespesa
   AND (:idDetalheDespesa IS NULL OR id_DetalheDespesa = :idDetalheDespesa)
   AND id_Funcionario = :idFuncionario